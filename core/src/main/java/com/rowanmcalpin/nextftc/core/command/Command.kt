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

package com.rowanmcalpin.nextftc.core.command

import com.rowanmcalpin.nextftc.core.Subsystem

/**
 * A discrete unit of functionality that runs simultaneous to all other commands. 
 */
abstract class Command {

    /**
     * Whether this command has completed running. Often implemented using a getter function, 
     *      although it can be set directly for commands that stop instantly or never stop. Please
     *      note that in certain circumstances the command will be stopped *before* this evaluates
     *      to true, most notably during subsystem conflicts or when the OpMode has been stopped.
     */
    abstract val isDone: Boolean

    /**
     * Whether this command can be stopped due to a conflict of [Subsystem]s.
     */
    open val interruptible = true

    /**
     * A set of all Subsystems this command implements. 
     */
    open val subsystems: Set<Subsystem> = mutableSetOf()

    /**
     * Called once when the command is first started
     */
    open fun start() { }

    /**
     * Called repeatedly while the command is running. 
     * 
     *  IMPORTANT: the time this function takes to
     *      run should be as close to 0 as possible, to maximize loop speed and increase 
     *      responsiveness.
     */
    open fun update() { }

    /**
     * Called once when the command is stopped.
     * @param interrupted whether this command was interrupted 
     */
    open fun stop(interrupted: Boolean) { }

    /**
     * Allows you to directly "call" a command
     */
    operator fun invoke() {
        CommandManager.scheduleCommand(this)
    }
}