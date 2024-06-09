/*
    Copyright (c) 2022 Atomic Robotics (https://atomicrobotics3805.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.
*/
@file:Suppress("PropertyName")

package com.rowanmcalpin.nextftc.driving.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix
import org.firstinspires.ftc.robotcore.external.navigation.*
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.TelemetryController
import com.rowanmcalpin.nextftc.trajectories.Pose2d
import com.rowanmcalpin.nextftc.trajectories.inchesToMm
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer as FTCVuforiaLocalizer
import java.util.*

/**
 * Determines the robot's absolute position based on its position relative to Vuforia targets. This
 * technically isn't a localizer, since localizers determine position relative to previous position.
 * To use this object, copy it into the proper package and change the three displacement constants.
 *
 * This class is currently broken. We are working to resolve the issue.
 */
@Suppress("Unused", "MemberVisibilityCanBePrivate")
class VuforiaLocalizer(val constants: VuforiaConstants) : SubsystemLocalizer {

    val cameraLocationOnRobot: OpenGLMatrix = OpenGLMatrix
            .translation(constants.CAMERA_FORWARD_DISPLACEMENT, constants.CAMERA_LEFT_DISPLACEMENT, constants.CAMERA_VERTICAL_DISPLACEMENT)
            .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES, 90f, 90f, 0f))

    override var poseEstimate: Pose2d
        get() = lastLocation?.let { Pose2d(it) } ?: Pose2d() + offset
        // set should almost never be used
        set(value) {
            offset = value - poseEstimate
        }
    override var poseVelocity: Pose2d? = null

    private var offset = Pose2d()

    // Since ImageTarget trackables use mm to specify their dimensions, we must use mm for all the physical dimension.
    private val mmTargetHeight = 6.0.inchesToMm.toFloat()

    private val halfField = 72.0.inchesToMm.toFloat()
    private val oneAndHalfTile = 36.0.inchesToMm.toFloat()
    private val halfTile = 12.0.inchesToMm.toFloat()

    private var lastLocation: OpenGLMatrix? = null
    private lateinit var vuforia: FTCVuforiaLocalizer

    private var targetVisible = false
    private var targets: VuforiaTrackables? = null
    private val allTrackables: ArrayList<VuforiaTrackable> = arrayListOf()

    /**
     * Initializes the webcam & sets up Vuforia
     */
    override fun initialize() {
        /*
         * Retrieve the camera we are to use.
         */
        val webcam = Constants.opMode.hardwareMap.get(WebcamName::class.java, "Webcam 1")

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC screen);
         * If no camera monitor is desired, use the parameter-less constructor instead (commented out below).
         * Note: A preview window is required if you want to view the camera stream on the Driver Station Phone.
         */
        val cameraMonitorViewId: Int = Constants.opMode.hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", Constants.opMode.hardwareMap.appContext.packageName)
        val parameters = FTCVuforiaLocalizer.Parameters(cameraMonitorViewId)

        parameters.vuforiaLicenseKey = constants.VUFORIA_KEY

        /*
         * We also indicate which camera on the RC we wish to use.
         */
        parameters.cameraName = webcam

        // Make sure extended tracking is disabled for this program.
        parameters.useExtendedTracking = false

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters)

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targets = vuforia.loadTrackablesFromAsset("FreightFrenzy")

        allTrackables.addAll(targets!!)

        //Set the position of the perimeter targets with relation to origin (center of field)
        /*
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of *transformation matrices.*
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See [Transformation Matrix](https://en.wikipedia.org/wiki/Transformation_matrix)
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the [OpenGLMatrix] class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         * - The X axis runs from your left to the right. (positive from the center to the right)
         * - The Y axis runs from the Red Alliance Station towards the other side of the field
         * where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         * - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         * coordinate system (the center of the field), facing up.
         */

        // Name and locate each trackable object
        identifyTarget(0, "Blue Storage",       -halfField,  oneAndHalfTile, mmTargetHeight, 90f, 0f, 90f)
        identifyTarget(1, "Blue Alliance Wall",  halfTile,   halfField,      mmTargetHeight, 90f, 0f, 0f)
        identifyTarget(2, "Red Storage",        -halfField, -oneAndHalfTile, mmTargetHeight, 90f, 0f, 90f)
        identifyTarget(3, "Red Alliance Wall",   halfTile,  -halfField,      mmTargetHeight, 90f, 0f, 180f)

        //
        // Create a transformation matrix describing where the phone is on the robot.
        //
        // Info:  The coordinate frame for the robot looks the same as the field.
        // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
        // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
        //
        // For a WebCam, the default starting orientation of the camera is looking UP (pointing in the Z direction),
        // with the wide (horizontal) axis of the camera aligned with the X axis, and
        // the Narrow (vertical) axis of the camera aligned with the Y axis
        //
        // But, this program assumes that the camera is actually facing forward out the front of the robot.
        // So, the "default" camera position requires two rotations to get it oriented correctly.
        // 1) First it must be rotated +90 degrees around the X axis to get it horizontal (it's now facing out the right side of the robot)
        // 2) Next it must be be rotated +90 degrees (counter-clockwise) around the Z axis to face forward.
        //
        // Finally the camera can be translated to its actual mounting position on the robot.

        /* Let all the trackable listeners know where the phone is.  */
        for (trackable in allTrackables) {
            (trackable.listener as VuforiaTrackableDefaultListener).setCameraLocationOnRobot(parameters.cameraName!!, cameraLocationOnRobot)
        }

        // Note: To use the remote camera preview:
        // AFTER you hit Init on the Driver Station, use the "options menu" to select "Camera Stream"
        // Tap the preview window to receive a fresh image.
        targets!!.activate()
    }

    override fun update() {
        // check all the trackable targets to see which one (if any) is visible.
        targetVisible = false
        for (trackable in allTrackables) {
            if ((trackable.listener as VuforiaTrackableDefaultListener).isVisible) {
                TelemetryController.telemetry.addData("Visible Target", trackable.name)
                targetVisible = true

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                val robotLocationTransform = (trackable.listener as VuforiaTrackableDefaultListener).updatedRobotLocation
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform
                }
                break
            }
        }
    }

    /**
     * Determines the name & location of a target
     */
    fun identifyTarget(
        targetIndex: Int, targetName: String?,
        dx: Float, dy: Float, dz: Float, rx: Float, ry: Float, rz: Float) {
        val aTarget = targets!![targetIndex]
        aTarget.name = targetName
        aTarget.location = OpenGLMatrix.translation(dx, dy, dz).multiplied(
            Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, rx, ry, rz)
        )
    }
}

interface VuforiaConstants {
    val VUFORIA_KEY: String
    val CAMERA_FORWARD_DISPLACEMENT: Float
    val CAMERA_VERTICAL_DISPLACEMENT: Float
    val CAMERA_LEFT_DISPLACEMENT: Float
    val DELAY_TIME_MILLIS: Double
}