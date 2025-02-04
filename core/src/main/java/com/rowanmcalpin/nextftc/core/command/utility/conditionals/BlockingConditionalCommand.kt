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

package com.rowanmcalpin.nextftc.core.command.utility.conditionals

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * This command behaves as an `if` statement, and schedules commands based on the result of the if 
 * statement. It is blocking, meaning `isDone` will not return `true` until the scheduled command 
 * has completed running.
 * @param condition the condition to reference
 * @param trueCommand the command to schedule if the reference is true
 * @param falseCommand the command to schedule if the reference is false
 */
class BlockingConditionalCommand(
    private val condition: () -> Boolean,
    private val trueCommand: () -> Command,
    private val falseCommand: (() -> Command)? = null
): Command() {

    private lateinit var selectedCommand: Command

    override val isDone: Boolean
        get() {
            if (this::selectedCommand.isInitialized) {
                return selectedCommand.isDone
            }
            return false
        }

    override fun start() {
        if (condition.invoke()) {
            selectedCommand = trueCommand.invoke()
            CommandManager.scheduleCommand(selectedCommand)
        } else {
            if (falseCommand != null) {
                selectedCommand = falseCommand.invoke()
                CommandManager.scheduleCommand(selectedCommand)
            }
        }
    }
}