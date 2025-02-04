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

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand

/**
 * A button control that has a value of true or false.
 * @param button the value of the button to watch
 */
class Button(private val button: () -> Boolean): Control() {
    /**
     * This command will be scheduled every time the button is pressed
     */
    var pressedCommand: () -> Command = { NullCommand() }

    /**
     * This command will be scheduled every time the button is released
     */
    var releasedCommand: () -> Command = { NullCommand() }

    /**
     * This command will be scheduled every update that the button is held down
     */
    var heldCommand: () -> Command = { NullCommand() }
    
    /**
     * This command will be scheduled whenever the state changes
     */
    var stateChangeCommand: () -> Command = { NullCommand() }

    override fun update() {
        updateState(button())
        
        if (state) {
            CommandManager.scheduleCommand(heldCommand())
        }
        
        if (risingState) {
            CommandManager.scheduleCommand(pressedCommand())
        }
        
        if (fallingState) {
            CommandManager.scheduleCommand(releasedCommand())
        }
        
        if (stateChanged) {
            CommandManager.scheduleCommand(stateChangeCommand())
        }
    }
}