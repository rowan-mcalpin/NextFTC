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

package com.rowanmcalpin.nextftc.ftc.driving

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick
import com.rowanmcalpin.nextftc.ftc.hardware.Drivetrain
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable

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