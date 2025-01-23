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

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.SubsystemGroup
import com.rowanmcalpin.nextftc.core.command.CommandManager

class SubsystemComponent(vararg var subsystems: Subsystem): NextComponent {
    override fun preInit() {
        expandSubsystems()
        initSubsystems()
    }

    override fun preWaitForStart() {
        subsystems.forEach {
            it.periodic()

            // Check if there are any commands running that use the subsystem, or if we can safely
            // schedule its default command
            if (!CommandManager.hasCommandsUsing(it)) {
                CommandManager.scheduleCommand(it.defaultCommand)
            }
        }
    }

    override fun preUpdate() {
        subsystems.forEach {
            it.periodic()

            // Check if there are any commands running that use the subsystem, or if we can safely
            // schedule its default command
            if (!CommandManager.hasCommandsUsing(it)) {
                CommandManager.scheduleCommand(it.defaultCommand)
            }
        }
    }

    /**
     * Called internally to initialize subsystems.
     */
    private fun initSubsystems() {
        subsystems.forEach {
            it.initialize()
        }
    }

    /**
     * Expands SubsystemGroups into a single-layer array (and puts that back into the [subsystems]
     * array)
     */
    private fun expandSubsystems() {
        val expanded = mutableListOf<Subsystem>()

        for (subsystem in subsystems) {
            if (subsystem is SubsystemGroup) {
                expanded += expandSubsystemGroup(subsystem)
            }
            expanded += subsystem
        }

        subsystems = expanded.toTypedArray()
    }

    /**
     * Expands a subsystem group (recursively)
     */
    private fun expandSubsystemGroup(group: SubsystemGroup): Array<Subsystem> {
        val expanded = mutableListOf<Subsystem>()

        for (child in group.subsystems) {
            if (child is SubsystemGroup) {
                expanded += expandSubsystemGroup(child)
            }
            expanded += child
        }

        return expanded.toTypedArray()
    }
}