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

package com.rowanmcalpin.nextftc.ftc.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import com.rowanmcalpin.nextftc.core.command.Command

class GamepadManager(gamepad1: Gamepad, gamepad2: Gamepad) {
    val gamepad1: GamepadEx = GamepadEx(gamepad1)
    val gamepad2: GamepadEx = GamepadEx(gamepad2)
    
    fun updateGamepads() {
        gamepad1.update()
        gamepad2.update()
    }
    
    inner class GamepadUpdaterCommand: Command() {
        override val isDone: Boolean = false

        override fun update() {
            updateGamepads()
        }
    }
}