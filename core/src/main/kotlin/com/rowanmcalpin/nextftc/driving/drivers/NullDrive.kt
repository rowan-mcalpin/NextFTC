package com.rowanmcalpin.nextftc.driving.drivers

import com.acmerobotics.roadrunner.drive.DriveSignal
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.utility.Delay
import com.rowanmcalpin.nextftc.driving.DriverControlled
import com.rowanmcalpin.nextftc.driving.MecanumDriveConstants
import com.rowanmcalpin.nextftc.driving.NullDriveConstants
import com.rowanmcalpin.nextftc.driving.localizers.NullLocalizer
import com.rowanmcalpin.nextftc.driving.localizers.SubsystemLocalizer
import com.rowanmcalpin.nextftc.hardware.MotorEx
import java.util.*

/**
 * This object is used by default as the Driver in an opmode. It does not control any motors on the robot.
 */
@Suppress("unused")
class NullDrive: Driver(NullDriveConstants(), NullLocalizer(), { Pose2d(0.0, 0.0, 0.0) }) {
    override val velConstraint: MinVelocityConstraint
        get() = MinVelocityConstraint(
            listOf(
                AngularVelocityConstraint(constants.MAX_ANG_VEL),
                MecanumVelocityConstraint(constants.MAX_VEL, constants.TRACK_WIDTH)
            )
        )
    override val rawExternalHeading: Double
        get() = 0.0
    override val externalHeadingVelocity: Double
        get() = 0.0

    override fun setDrivePower(drivePower: Pose2d) {
    }

    override fun setDriveSignal(driveSignal: DriveSignal) {
    }

    override fun driverControlled(gamepad: Gamepad): Command {
        return Delay(1.0)
    }
}