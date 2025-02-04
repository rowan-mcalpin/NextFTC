/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 *     Copyright (C) 2025 Rowan McAlpin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.rowanmcalpin.nextftc.pedro

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick
import com.rowanmcalpin.nextftc.ftc.hardware.Drivetrain

/**
 * Uses the joystick inputs to drive the robot
 *
 * @param driveJoystick The joystick to use for forward and strafe movement
 * @param turnJoystick The joystick to use for turning
 * @param robotCentric Whether to use robot centric or field centric movement
 * @param invertDrive whether to invert the drive joystick
 * @param invertTurn whether to invert the turn joystick
 * @param invertStrafe whether to invert the strafe joystick
 */
class DriverControlled @JvmOverloads constructor(val driveJoystick: Joystick, val turnJoystick: Joystick, val robotCentric: Boolean = true,
    val invertDrive: Boolean = false, val invertTurn: Boolean = false, val invertStrafe: Boolean = false): Command() {

    @JvmOverloads
    /**
     * @param gamepad The gamepad to use the joysticks from
     * @param robotCentric Whether to use robot centric or field centric movement
     * @param invertDrive whether to invert the drive joystick
     * @param invertTurn whether to invert the turn joystick
     * @param invertStrafe whether to invert the strafe joystick
     */
    constructor(gamepad: GamepadEx, robotCentric: Boolean = true, invertDrive: Boolean = false, invertTurn: Boolean = false, invertStrafe: Boolean = false): this(gamepad.leftStick, gamepad.rightStick, robotCentric, invertDrive, invertTurn, invertStrafe)

    override val isDone: Boolean = false

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    override fun start() {
        if (PedroData.follower == null) {
            throw FollowerNotInitializedException()
        }
        PedroData.follower!!.startTeleopDrive()
    }
    
    override fun update() {
        PedroData.follower!!.setTeleOpMovementVectors(driveJoystick.y.toDouble() * if(invertDrive) -1 else 1,
            driveJoystick.x.toDouble() * if(invertStrafe) -1 else 1, turnJoystick.x.toDouble() * if(invertTurn) -1 else 1, robotCentric)
    }
}