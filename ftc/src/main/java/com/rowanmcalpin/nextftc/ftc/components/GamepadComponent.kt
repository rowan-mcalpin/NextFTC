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

package com.rowanmcalpin.nextftc.ftc.components

import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager

class GamepadComponent: NextComponent {
    override fun preInit() {
        if (OpModeData.gamepad1 == null || OpModeData.gamepad2 == null) {
            throw UninitializedPropertyAccessException("gamepad has not been initialized")
        }
        GamepadManager.initialize(OpModeData.gamepad1!!, OpModeData.gamepad2!!)
    }

    override fun postInit() {
        CommandManager.scheduleCommand(GamepadManager.GamepadUpdaterCommand())
    }
}