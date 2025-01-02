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
 * This behaves like the command-form of a switch statement. You provide it with a value to
 * reference, and a list of options of outcomes. It is non-blocking, meaning `isDone` is true 
 * immediately, regardless of how long the scheduled command(s) take to run.
 * @param value the value to reference
 * @param outcomes all of the options for outcomes
 * @param default the command to schedule if none of the outcomes are fulfilled
 */
class PassiveSwitchCommand(
    private val value: () -> Any,
    private vararg val outcomes: Pair<Any, () -> Command>,
    private val default: (() -> Command)? = null
): Command() {

    /**
     * The commands that have been selected
     */
    private val selectedCommands: MutableList<Command> = mutableListOf()

    override val isDone: Boolean
        get() = true
    
    override fun start() {
        val value = this.value.invoke()
        outcomes.forEach {
            if (it.first == value) {
                selectedCommands.add(it.second.invoke())
                CommandManager.scheduleCommand(selectedCommands.last())
            }
        }
        
        if (selectedCommands.size == 0) {
            if (default != null) {
                selectedCommands.add(default.invoke())
                CommandManager.scheduleCommand(selectedCommands.last())
            }
        }
    }
    
}