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
package com.rowanmcalpin.nextftc.controls

import com.rowanmcalpin.nextftc.CommandScheduler
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.GamepadEx

/**
 * This class manages the controls for TeleOp OpModes. In your project, you should create your own
 * Controls class that inherits from this one and overrides the registerCommands function. If you
 * want to register a command, type a line into registerCommands with the following format:
 * gamepad.button.command = Subsystem.command
 * For example, that could look like this:
 * gamepad1.a.startCommand = Lift.toHigh
 * If you used the line above, then whenever you pressed the "a" button on gamepad1 it would move
 * the lift to the high position.
 */
abstract class Controls {

    protected lateinit var gamepad1: GamepadEx
    protected lateinit var gamepad2: GamepadEx

    /**
     * Registers gamepad1 and gamepad2 with the CommandScheduler so that the CommandScheduler can
     * update them every loop
     */
    fun registerGamepads() {
        gamepad1 = GamepadEx(Constants.opMode.gamepad1)
        gamepad2 = GamepadEx(Constants.opMode.gamepad2)
        CommandScheduler.registerGamepads(gamepad1, gamepad2)
    }

    /**
     * Registers commands on the gamepads.
     * Directions for registering commands are in the class docs.
     */
    abstract fun registerCommands()
}