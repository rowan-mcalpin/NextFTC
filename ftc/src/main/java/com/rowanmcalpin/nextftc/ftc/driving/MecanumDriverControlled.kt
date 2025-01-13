/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
class MecanumDriverControlled(val motors: Array<out Controllable>, val driveJoystick: Joystick, val turnJoystick: Joystick, val robotCentric: Boolean, val imu: IMU?): Command() {
    /**
     * @param motors: An array of your drive motors in the following order: left front, right front, left back, right back
     * @param driveJoystick: The joystick to use for forward and strafe movement
     * @param turnJoystick: The joystick to use for turning
     */
    constructor(motors: Array<out Controllable>, driveJoystick: Joystick, turnJoystick: Joystick): this(motors, driveJoystick, turnJoystick, true, null)

    /**
     * @param motors: An array of your drive motors in the following order: left front, right front, left back, right back
     * @param gamepad: The gamepad to use the joysticks from
     * @param robotCentric: Whether to use robot centric or field centric movement
     */
    constructor(motors: Array<out Controllable>, gamepad: GamepadEx, robotCentric: Boolean, imu: IMU): this(motors, gamepad.leftStick, gamepad.rightStick, robotCentric, imu)

    /**
     * @param motors: An array of your drive motors in the following order: left front, right front, left back, right back
     * @param gamepad: The gamepad to use the joysticks from
     */
    constructor(motors: Array<out Controllable>, gamepad: GamepadEx): this(motors, gamepad.leftStick, gamepad.rightStick, true, null)

    override val isDone: Boolean = false

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    var scalar = 1.0
    private var offset: Double = 0.0

    var orientation: Double
        get() = imu!!.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS) - offset
        set(value) { offset = imu!!.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS) - value }

    override fun start() {
        if (motors.size != 4) throw IllegalArgumentException("motors must be an array of 4 motors.")
        if (!robotCentric && imu == null) throw IllegalArgumentException("imu must be provided if robotCentric is false.")
    }

    override fun update() {
        var y = driveJoystick.y.toDouble()
        var x = driveJoystick.x.toDouble()
        val rx = turnJoystick.x.toDouble()

        if (!robotCentric) {
            val botHeading = orientation

            // Rotate the movement direction counter to the bot's rotation
            var rotX = x * cos(-botHeading) - y * sin(-botHeading)
            val rotY = x * sin(-botHeading) + y * cos(-botHeading)

            rotX *= 1.1 // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            val denominator = max(abs(rotY) + abs(rotX) + abs(rx), 1.0)
            val frontLeftPower = (rotY + rotX + rx) / denominator
            val backLeftPower = (rotY - rotX + rx) / denominator
            val frontRightPower = (rotY - rotX - rx) / denominator
            val backRightPower = (rotY + rotX - rx) / denominator

            motors[0].power = (frontLeftPower * scalar)
            motors[1].power = (frontRightPower * scalar)
            motors[2].power = (backLeftPower * scalar)
            motors[3].power = (backRightPower * scalar)
        } else {
            val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
            val frontLeftPower = (y + x + rx) / denominator
            val backLeftPower = (y - x + rx) / denominator
            val frontRightPower = (y - x - rx) / denominator
            val backRightPower = (y + x - rx) / denominator

            motors[0].power = (frontLeftPower)
            motors[1].power = (frontRightPower)
            motors[2].power = (backLeftPower)
            motors[3].power = (backRightPower)
        }
    }
}