package com.rowanmcalpin.nextftc.ftc.driving

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick
import com.rowanmcalpin.nextftc.ftc.hardware.Drivetrain
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx

/**
 * Drives a differential drivetrain as a tank drive.
 * @param leftMotor: The motor(s) on the left side of the drivetrain
 * @param rightMotor: The motor(s) on the right side of the drivetrain
 * @param leftJoystick: The joystick to use to control the left side of the drivetrain
 * @param rightJoystick: The joystick to use to control the right side of the drivetrain
 */
class DifferentialTankDriverControlled(val leftMotor: Controllable, val rightMotor: Controllable, val leftJoystick: Joystick, val rightJoystick: Joystick) : Command() {

    /**
     * @param leftMotor: The motor(s) on the left side of the drivetrain
     * @param rightMotor: The motor(s) on the right side of the drivetrain
     * @param gamepad: The gamepad to use the joysticks from
     */
    constructor(leftMotor: Controllable, rightMotor: Controllable, gamepad: GamepadEx): this(leftMotor, rightMotor, gamepad.leftStick, gamepad.rightStick)

    override val isDone: Boolean = false

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    override fun update() {
        leftMotor.power = leftJoystick.y.toDouble()
        rightMotor.power = rightJoystick.y.toDouble()
    }
}