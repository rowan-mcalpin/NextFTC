package com.rowanmcalpin.nextftc.driving

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.hardware.Gamepad
import com.rowanmcalpin.nextftc.Command
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.Constants.color
import com.rowanmcalpin.nextftc.Constants.drive
import com.rowanmcalpin.nextftc.subsystems.Subsystem
import com.rowanmcalpin.nextftc.trajectories.toRadians
import kotlin.math.*

/**
 * Controls the robot manually using a gamepad. Left stick up/down moves the robot forwards/backwards, left stick left/
 * right moves the robot left/right, right stick left/right makes the robot turn left/right
 *
 * @param gamepad the gamepad that controls the driving
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 * @param reverseStrafe whether to reverse the left/right direction
 * @param reverseStraight whether to reverse the forwards/backwards direction
 * @param reverseTurn whether to reverse the turning left/right direction
 */
class DriverControlled(
    private val gamepad: Gamepad,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true,
    private val pov: POV = POV.ROBOT_CENTRIC,
    private val reverseStrafe: Boolean = true,
    private val reverseStraight: Boolean = false,
    private val reverseTurn: Boolean = true
) : com.rowanmcalpin.nextftc.Command() {

    enum class POV {
        ROBOT_CENTRIC,
        FIELD_CENTRIC,
        DRIVER_CENTRIC
    }

    override val _isDone = false

    /**
     * Calculates and sets the robot's drive power
     */
    override fun execute() {
        val drivePower: Pose2d
        if (pov != POV.ROBOT_CENTRIC) {
            val angle: Double = atan2((if(reverseStraight) -gamepad.left_stick_y else gamepad.left_stick_y), (if(reverseStrafe) -gamepad.left_stick_x else gamepad.left_stick_x)).toDouble()

            var adjustedAngle = angle + drive.poseEstimate.heading

            if (pov == POV.DRIVER_CENTRIC) {
                if (color == Constants.Color.BLUE) {
                    adjustedAngle -= 90.0.toRadians
                }
                else {
                    adjustedAngle += 90.0.toRadians
                }
            }
            val totalPower = sqrt(gamepad.left_stick_y.pow(2) + gamepad.left_stick_x.pow(2))
            drivePower = Pose2d(
                totalPower * sin(adjustedAngle),
                totalPower * cos(adjustedAngle),
                if (reverseTurn) -1 else { 1 } * (gamepad.right_stick_x).toDouble()
            )
        }
        else {
            drivePower = Pose2d(
                if (reverseStraight) -1 else { 1 } * (gamepad.left_stick_y).toDouble(),
                if (reverseStrafe) -1 else { 1 } * (gamepad.left_stick_x).toDouble(),
                if (reverseTurn) -1 else { 1 } * (gamepad.right_stick_x).toDouble()
            )
        }

        drive.setDrivePower(drivePower * drive.driverSpeed)
    }
}