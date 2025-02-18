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

package com.rowanmcalpin.nextftc.core.command

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.SubsystemGroup
import com.rowanmcalpin.nextftc.core.command.groups.CommandGroup

/**
 * This is the central controller for running commands in NextFTC. 
 */
object CommandManager {

    /**
     * Actively running commands.
     */
    val runningCommands = mutableListOf<Command>()

    /**
     * Commands that haven't been started yet.
     */
    private val commandsToSchedule = mutableListOf<Command>()
    private val commandsToCancel = mutableMapOf<Command, Boolean>()

    /**
     * This function should be run repeatedly every loop. It adds commands if the corresponding
     * Gamepad buttons are being pushed, it runs the periodic functions in Subsystems, it schedules
     * & cancels any commands that need to be started or stopped, and it executes running
     * commands. The reason why it uses a separate function to cancel commands instead of cancelling
     * them itself is because removing items from a list while iterating through that list is a
     * wacky idea.
     */
    // exercise is healthy (and fun!)
    fun run() {
        scheduleCommands()
        cancelCommands()
        for (command in runningCommands) {
            command.update()
            
            if (command.isDone) {
                commandsToCancel += Pair(command, false)
            }
        }
    }

    /**
     * Schedules a command. When multiple commands are scheduled, each of them run in parallel.
     * @param command the command to be scheduled
     */
    fun scheduleCommand(command: Command) {
        commandsToSchedule += command
    }

    /**
     * Cancels every command. This function should generally only be used when an OpMode ends.
     */
    fun cancelAll() {
        for (command in runningCommands) {
            commandsToCancel += Pair(command, true)
        }
        runningCommands.clear()
        cancelCommands()
        commandsToSchedule.clear()
    }

    /**
     * Returns whether or not there are commands running
     */
    fun hasCommands(): Boolean = runningCommands.isNotEmpty()

    /**
     * Initializes every command in the commandsToSchedule list.
     */
    fun scheduleCommands() {
        // We have to do it like this in order to prevent concurrentmodificationexceptions when
        // scheduling command groups
        val newCommands = commandsToSchedule.toList()
        // Clear before looping so that we don't clear out any commands that get scheduled inside of
        // init functions
        commandsToSchedule.clear()

        for(command in newCommands) {
            initCommand(command)
        }
    }

    /**
     * Cancels every command in the commandsToCancel list.
     */
    fun cancelCommands() {
        // We have to do it like this in order to prevent concurrentmodificationexceptions when cancelling certain commandgroups
        val commands = commandsToCancel.toMap()
        // Clear before looping so we don't clear any commands that get cancelled inside of stop functions
        commandsToCancel.clear()

        for(pair in commands) {
            cancel(pair.key, pair.value)
        }
        commandsToCancel.clear()
    }

    /**
     * Initializes a command. This function first scans to find any conflicts (other commands using
     * the same subsystem). It then checks to see if any of those commands are not interruptible. If
     * some of them aren't interruptible, it ends the initialization process and does not schedule
     * the new command. Otherwise, it cancels the conflicts, runs the new command's start function,
     * and adds it to the list of runningCommands.
     * @param command the new command being initialized
     */
    private fun initCommand(command: Command) {
        val subsystems = expandSubsystems(command.subsystems)

        for (otherCommand in runningCommands) {
            val otherSubsystems = expandSubsystems(otherCommand.subsystems)

            for (requirement in subsystems) {
                if (otherSubsystems.contains(requirement)) {
                    if (otherCommand.interruptible) {
                        commandsToCancel += Pair(otherCommand, true)
                    } else {
                        return
                    }
                }
            }
        }
        
        command.start()
        runningCommands += command
    }

    /**
     * Given a set of subsystems (including groups), this extracts the subsystems from within those
     * groups and returns the set of the subsystems in the set plus the subsystems that are children
     * of the groups (if any).
     * @param subsystems the set of subsystems to expand
     * @return the expanded set of subsystems
     */
    fun expandSubsystems(subsystems: Set<Subsystem>): Set<Subsystem> {
        val expanded = mutableListOf<Subsystem>()

        for (subsystem in subsystems) {
            if (subsystem is SubsystemGroup) {
                expanded += expandSubsystemGroup(subsystem)
            }
        }

        return expanded.toSet()
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

    /**
     * Ends a command and removes it from the runningCommands list.
     * @param command the command being cancelled
     * @param interrupted whether or not that command was interrupted, such as the OpMode is stopped
     *                    prematurely
     */
    private fun cancel(command: Command, interrupted: Boolean = false) {
        command.stop(interrupted)
        runningCommands -= command
    }

    /**
     * Calls the findCommands() function and uses the first result, or null if there are none
     * @param check the lambda used to determine what kind of command should be found
     * @param commands the list of commands to scan, uses runningCommands by default
     */
    private fun findCommand(check: (Command) -> Boolean, commands : List<Command> = runningCommands) =
        findCommands(check, commands).firstOrNull()

    /**
     * Returns a list of every command in the given list that passes a check. Also scans
     * CommandGroups by recursively calling itself.
     * @param check the lambda used to determine what kind of commands should be found
     * @param commands the list of commands to scan, uses runningCommands by default
     */
    private fun findCommands(check: (Command) -> Boolean, commands : List<Command> = runningCommands):
            List<Command> {
        val foundCommands = mutableListOf<Command>()
        for (command in commands) {
            if (check.invoke(command))
                foundCommands.add(command)
            if (command is CommandGroup) {
                val c = findCommand(check, command.children)
                if (c != null) foundCommands.add(c)
            }
        }
        return foundCommands
    }
    
    fun hasCommandsUsing(subsystem: Subsystem): Boolean {
        return runningCommands.any { it.subsystems.contains(subsystem) }
    }

    fun findConflicts(command: Command): List<Command> {
        val foundConflicts: MutableList<Command> = mutableListOf()

        for (otherCommand in runningCommands) {
            for (requirement in command.subsystems) {
                if (otherCommand.subsystems.contains(requirement)) {
                    foundConflicts += otherCommand
                }
            }
        }

        return foundConflicts
    }

    fun cancelCommand(command: Command) {
        if (runningCommands.contains(command)) {
            commandsToCancel += Pair(command, true)
        }
    }
}