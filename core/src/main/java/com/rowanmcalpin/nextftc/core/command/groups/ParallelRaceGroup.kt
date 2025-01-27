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
     * Private variable to track whether this group is completed
     */
    private var _isDone = false

    /**
     * This will return false until one of its children is done
     */
    override val isDone: Boolean
        get() = _isDone

    /**
     * Overrides the [Command.subsystems] variable to inherit all subsystems from all of its children.
     */
    override val subsystems: Set<Subsystem>
        get() = children.flatMap { it.subsystems }.toSet()


    /**
     * This function starts all of the children (but ensures that only one command will use each subsystem)
     * If there are multiple commands using a subsystem, only the *first* command gets started & run; the
     * other WILL NOT happen.
     */
    override fun start() {
        super.start()

        val subsystems = mutableListOf<Subsystem>()

        val badCommands = mutableListOf<Command>()

        children.forEach {
            var hasNoConflicts = true
            for (subsystem in it.subsystems) {
                if (subsystems.contains(subsystem)) {
                    hasNoConflicts = false
                }
            }
            if (hasNoConflicts) {
                subsystems.addAll(it.subsystems)
                it.start()
            } else {
                // This means that there was already a command that used one or more of this command's subsystems
                // So, we can't start it, and we also shouldn't run it, so we'll need to remove it.
                badCommands += it
            }
        }
        // Cancel running any problematic commands (without calling their stop function)
        badCommands.forEach {
            children.remove(it)
        }
    }

    override fun update() {
        children.forEach {
            it.update()

            if (it.isDone) {
                _isDone = true
            }
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
            it.stop(interrupted)
        }
    }
}