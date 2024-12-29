package com.rowanmcalpin.nextftc.pedro

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick

/**
 * Uses the joystick inputs to drive the robot
 *
 * @param driveJoystick The joystick to use for forward and strafe movement
 * @param turnJoystick The joystick to use for turning
 * @param robotCentric Whether to use robot centric or field centric movement
 */
class DriverControlled(val driveJoystick: Joystick, val turnJoystick: Joystick, val robotCentric: Boolean): Command() {

    /**
     * @param driveJoystick The joystick to use for forward and strafe movement
     * @param turnJoystick The joystick to use for turning
     */
    constructor(driveJoystick: Joystick, turnJoystick: Joystick): this(driveJoystick, turnJoystick, true)

    /**
     * @param gamepad The gamepad to use the joysticks from
     * @param robotCentric Whether to use robot centric or field centric movement
     */
    constructor(gamepad: GamepadEx, robotCentric: Boolean): this(gamepad.leftStick, gamepad.rightStick, robotCentric)

    /**
     * @param gamepad The gamepad to use the joysticks from
     */
    constructor(gamepad: GamepadEx): this(gamepad.leftStick, gamepad.rightStick, true)

    override val isDone: Boolean = false

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    override fun start() {
        if (PedroData.follower == null) {
            throw FollowerNotInitializedException()
        }
        PedroData.follower!!.startTeleopDrive()
    }
    
    override fun update() {
        PedroData.follower!!.setTeleOpMovementVectors(driveJoystick.y.toDouble(),
            driveJoystick.x.toDouble(), turnJoystick.x.toDouble(), robotCentric)
    }
}