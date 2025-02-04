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

package com.rowanmcalpin.nextftc.core.command.groups

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * A [CommandGroup] that runs its children one at a time.
 */
class SequentialGroup(vararg commands: Command): CommandGroup(*commands) {
    /**
     * This returns true once all of its children have finished running.
     */
    override val isDone: Boolean
        get() = children.size == 0

    /**
     * In a Sequential Group, we will start the first command and wait until it has completed
     * execution before starting the next.
     */
    override fun start() {
        super.start()
        CommandManager.scheduleCommand(children[0])
        CommandManager.scheduleCommands()
    }

    /**
     * Now, every update we must check if the currently active command is complete. If it is, remove
     * it and start the next one (if there is one).
     */
    override fun update() {
        // If the first child is done running, remove it and start the next one.
        if (children[0].isDone) {
            children.removeAt(0)

            // Now, if there is another command to run, start it. 
            if (children.size > 0) {
                CommandManager.scheduleCommand(children[0])
            }
        }
    }
}