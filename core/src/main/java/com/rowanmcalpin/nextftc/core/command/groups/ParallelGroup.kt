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

/**
 * A [CommandGroup] that runs all of its children simultaneously.
 */
class ParallelGroup(vararg commands: Command): CommandGroup(*commands) {
    /**
     * This will return false until all of its children are done
     */
    override val isDone: Boolean
        get() = children.all { it.isDone }

    private val finishedCommands: MutableList<Command> = mutableListOf()

    /**
     * In a Parallel Group, we need to immediately start all of our children
     */
    override fun start() {
        super.start()

        val subsystems = mutableListOf<Subsystem>()

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
                finishedCommands += it
            }
        }
        // Cancel running any problematic commands (without calling their stop function)
        finishedCommands.forEach {
            children.remove(it)
        }
        finishedCommands.clear()
    }

    override fun update() {
        // Cancel commands that need cancelling
        finishedCommands.forEach {
            it.stop(false)
            children.remove(it)
        }
        finishedCommands.clear()
        children.forEach {
            it.update()

            if (it.isDone) {
                finishedCommands += it
            }
        }
    }

    override fun stop(interrupted: Boolean) {
        children.forEach {
            it.stop(interrupted)
        }
    }

}