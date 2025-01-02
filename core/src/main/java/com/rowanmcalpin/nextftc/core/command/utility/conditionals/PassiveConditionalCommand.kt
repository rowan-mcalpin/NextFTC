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

package com.rowanmcalpin.nextftc.core.command.utility.conditionals

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * This command behaves as an `if` statement, and adds commands based on the result of the if 
 * statement. It is non-blocking, meaning `isDone` is true immediately, regardless of how long the 
 * scheduled command takes to run.
 * @param condition the condition to reference
 * @param trueCommand the command to schedule if the reference is true
 * @param falseCommand the command to schedule if the reference is false
 */
class PassiveConditionalCommand(
    private val condition: () -> Boolean,
    private val trueCommand: () -> Command,
    private val falseCommand: (() -> Command)? = null
): Command() {
    override val isDone: Boolean
        get() = true
    
    override fun start() {
        if (condition.invoke()) {
            CommandManager.scheduleCommand(trueCommand.invoke())
        } else {
            if (falseCommand != null) {
                CommandManager.scheduleCommand(falseCommand.invoke())
            }
        }
    }
}