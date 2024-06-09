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

package org.atomicrobotics3805.cflib.driving.localizers

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics
import com.acmerobotics.roadrunner.util.Angle
import org.atomicrobotics3805.cflib.driving.MecanumDriveConstants
import org.atomicrobotics3805.cflib.driving.drivers.MecanumDrive


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
