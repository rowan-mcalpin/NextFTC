package com.rowanmcalpin.nextftc.ftc.driving

import com.qualcomm.robotcore.hardware.IMU
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick
import com.rowanmcalpin.nextftc.ftc.hardware.Drivetrain
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin


/**
 * Drives a mecanum drivetrain
 * @param motors: An array of your drive motors in the following order: left front, right front, left back, right back
 * @param driveJoystick: The joystick to use for forward and strafe movement
 * @param turnJoystick: The joystick to use for turning
 * @param robotCentric: Whether to use robot centric or field centric movement
 */

class MecanumDriverControlled(val motors: Array<Controllable>, val driveJoystick: Joystick, val turnJoystick: Joystick, val robotCentric: Boolean, val imu: IMU?): Command() {
    /**
     * @param motors: An array of your drive motors in the following order: left front, right front, left back, right back
     * @param driveJoystick: The joystick to use for forward and strafe movement
     * @param turnJoystick: The joystick to use for turning
     */
    constructor(motors: Array<Controllable>, driveJoystick: Joystick, turnJoystick: Joystick): this(motors, driveJoystick, turnJoystick, true, null)

    /**
     * @param motors: An array of your drive motors in the following order: left front, right front, left back, right back
     * @param gamepad: The gamepad to use the joysticks from
     * @param robotCentric: Whether to use robot centric or field centric movement
     */
    constructor(motors: Array<Controllable>, gamepad: GamepadEx, robotCentric: Boolean, imu: IMU): this(motors, gamepad.leftStick, gamepad.rightStick, robotCentric, imu)

    /**
     * @param motors: An array of your drive motors in the following order: left front, right front, left back, right back
     * @param gamepad: The gamepad to use the joysticks from
     */
    constructor(motors: Array<Controllable>, gamepad: GamepadEx): this(motors, gamepad.leftStick, gamepad.rightStick, true, null)

    override val isDone: Boolean = false

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    override fun start() {
        if (motors.size != 4) throw IllegalArgumentException("motors must be an array of 4 motors.")
        if (!robotCentric && imu == null) throw IllegalArgumentException("imu must be provided if robotCentric is false.")
    }

    override fun update() {
        var y = -gamepad1.left_stick_y.toDouble()
        var x = gamepad1.left_stick_x.toDouble()
        val rx = gamepad1.right_stick_x.toDouble()

        if (!robotCentric) {
            val botHeading = imu!!.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS)


            // Rotate the movement direction counter to the bot's rotation
            x = x * cos(-botHeading) - y * sin(-botHeading)
            y = x * sin(-botHeading) + y * cos(-botHeading)
        }


        x *= 1.1 // Counteract imperfect strafing


        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
        val frontLeftPower = (y + x + rx) / denominator
        val backLeftPower = (y - x + rx) / denominator
        val frontRightPower = (y - x - rx) / denominator
        val backRightPower = (y + x - rx) / denominator

        motors[0].power = frontLeftPower
        motors[1].power = frontRightPower
        motors[2].power = backLeftPower
        motors[3].power = backRightPower
    }
}