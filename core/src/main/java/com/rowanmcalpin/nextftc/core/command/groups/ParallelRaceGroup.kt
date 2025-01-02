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

package com.rowanmcalpin.nextftc.core.command.groups

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * A [CommandGroup] that runs all of its children simultaneously until one of its children is done,
 * at which point it stops all of its children.
 */
class ParallelRaceGroup(vararg commands: Command): CommandGroup(*commands) {
    /**
     * Private variable to be able to manually set interruptible during creation.
     */
    private var _interruptible = true

    override val interruptible
        get() = _interruptible

    /**
     * This will return false until one of its children is done
     */
    override val isDone: Boolean
        get() = children.any { it.isDone }

    /**
     * Overrides the [Command.subsystems] variable to inherit all subsystems from all of its children.
     */
    override val subsystems: Set<Subsystem>
        get() = children.flatMap { it.subsystems }.toSet()


    /**
     * In a Parallel Group, we can just straight away add all of the commands to the CommandManager,
     * which can take care of the rest.
     */
    override fun start() {
        super.start()
        children.forEach {
            CommandManager.scheduleCommand(it)
        }
    }

    /**
     * Sets whether this command is [interruptible]. This functionality is similar to a builder class,
     * so you can use it inline with the Command Group declaration.
     * @param interruptible whether this group should be interruptible
     * @return this Command Group, with [interruptible] set to the value
     */
    fun setInterruptible(interruptible: Boolean): ParallelRaceGroup {
        _interruptible = interruptible
        return this
    }

    override fun stop(interrupted: Boolean) {
        children.forEach {
            CommandManager.cancelCommand(it)
        }
    }
}