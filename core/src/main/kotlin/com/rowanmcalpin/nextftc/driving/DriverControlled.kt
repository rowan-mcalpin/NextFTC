package com.rowanmcalpin.nextftc.driving

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.hardware.Gamepad
import com.rowanmcalpin.nextftc.Constants.drive
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.utility.CustomCommand
import com.rowanmcalpin.nextftc.subsystems.Subsystem
import kotlin.math.*


class DriverControlled(
    private val gamepad: Gamepad,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true,
    private val pov: POV = POV.ROBOT_CENTRIC,
    private val reverseStrafe: Boolean = true,
    private val reverseStraight: Boolean = false,
    private val reverseTurn: Boolean = true,
    private val lowMultiplier: Double = 0.65,
    private val highMultiplier: Double = 1.0
): Command() {

    var rotationOffset = 0.0

    val resetRotation: Command
        get() = CustomCommand(_start = { rotationOffset = drive.poseEstimate.heading })

    enum class POV {
        ROBOT_CENTRIC,
        FIELD_CENTRIC
    }

    override val _isDone = false

    /**
     * Calculates and sets the robot's drive power
     */
    override fun onExecute() {
        val drivePower: Pose2d
        if (pov == POV.FIELD_CENTRIC) {
            // Swapped x and y is intentional because roadrunner uses "robotics" x and y whereas gamepad uses "gaming" x and y
            val x: Double = (if(reverseStraight) -gamepad.left_stick_y else gamepad.left_stick_y).toDouble()
            val y: Double = (if(reverseStrafe) -gamepad.left_stick_x else gamepad.left_stick_x).toDouble()
            val rx: Double = (if(reverseTurn) -gamepad.right_stick_x else gamepad.right_stick_x).toDouble()

            /*
            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing
            */

            val heading = drive.poseEstimate.heading - rotationOffset
            val rotX = x * cos(-heading) - y * sin(-heading)
            val rotY = x * sin(-heading) + y * cos(-heading)



            // Denominator is the highest value of the motor powers, so we can have our motor powers between 0 and 1
            val denominator: Double = max(abs(rotY) + abs(rotX) + abs(rx), 1.0)
            drivePower = Pose2d(
                rotX / denominator,
                rotY / denominator,
                rx / denominator
            )
//            TelemetryController.telemetry.addData("Angle", angle.toString())
//            TelemetryController.telemetry.addData("Adjusted angle", adjustedAngle.toString())
//            TelemetryController.telemetry.addData("Drive power", drivePower.toString())
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