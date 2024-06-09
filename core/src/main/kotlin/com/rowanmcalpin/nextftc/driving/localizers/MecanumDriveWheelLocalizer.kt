package com.rowanmcalpin.nextftc.driving.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics
import com.acmerobotics.roadrunner.util.Angle
import com.rowanmcalpin.nextftc.driving.MecanumDriveConstants
import com.rowanmcalpin.nextftc.driving.drivers.MecanumDrive


/**
 * Determines position relative to previous position based on odometry wheels. Odometry wheels are
 * wheels that aren't attached to motors but are attached to encoders that can determine how much
 * the wheels have rotated. Based on how much they've rotated, we can determine the robot's
 * position. The reason why there are four constants assigned outside of the class is because we
 * need them to be outside the class to use them in the constructor.
 *
 * This class is currently broken. We are working to resolve the issue.
 */

class MecanumDriveWheelLocalizer(
    private val _drive: () -> MecanumDrive,
    private val useExternalHeading: Boolean = true
) : SubsystemLocalizer {
    private var _poseEstimate = Pose2d()
    override var poseEstimate: Pose2d
        get() = _poseEstimate
        set(value) {
            lastWheelPositions = emptyList()
            lastExtHeading = Double.NaN
            if (useExternalHeading) extHeadingOffset = value.heading - drive.rawExternalHeading
            _poseEstimate = value
        }
    override var poseVelocity: Pose2d? = null
        private set
    private var lastWheelPositions = emptyList<Double>()
    private var lastExtHeading = Double.NaN
    private var extHeadingOffset = 0.0

    private lateinit var drive: MecanumDrive
    /**
     * Initializes the encoders & sets their direction
     */
    override fun initialize() {
        drive = _drive.invoke()
    }

    override fun update() {
        drive.constants as MecanumDriveConstants
        val wheelPositions = drive.getWheelPositions()
        val extHeading = if (useExternalHeading) drive.rawExternalHeading else Double.NaN
        if (lastWheelPositions.isNotEmpty()) {
            val wheelDeltas = wheelPositions
                .zip(lastWheelPositions)
                .map { it.first - it.second }
            val robotPoseDelta = MecanumKinematics.wheelToRobotVelocities(
                wheelDeltas,
                drive.constants.TRACK_WIDTH,
                drive.constants.TRACK_WIDTH,
                (drive.constants as MecanumDriveConstants).LATERAL_MULTIPLIER
            )
            val finalHeadingDelta = if (useExternalHeading) {
                Angle.normDelta(extHeading - lastExtHeading)
            } else {
                robotPoseDelta.heading
            }
            _poseEstimate = Kinematics.relativeOdometryUpdate(
                _poseEstimate,
                Pose2d(robotPoseDelta.vec(), finalHeadingDelta)
            )
        }

        poseVelocity = MecanumKinematics.wheelToRobotVelocities(
            drive.getWheelVelocities(),
            drive.constants.TRACK_WIDTH,
            drive.constants.TRACK_WIDTH,
            (drive.constants as MecanumDriveConstants).LATERAL_MULTIPLIER
        )
        if (useExternalHeading) {
            poseVelocity = Pose2d(poseVelocity!!.vec(), drive.externalHeadingVelocity)
        }

        lastWheelPositions = wheelPositions
        lastExtHeading = extHeading
    }


}
