/*
    Copyright (c) 2022 Atomic Robotics (https://atomicrobotics3805.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.rowanmcalpin.nextftc

/**
 * Returns a sequential command group. Should be used in the following format:
 * val mySequentialCommandGroup: SequentialCommandGroup
 *     get() = sequential {
 *         +FirstCommand()
 *         +SecondCommand()
 *     }
 * @param block the list of commands in the group
 */
fun sequential(block: com.rowanmcalpin.nextftc.SequentialCommandGroup.() -> Unit): com.rowanmcalpin.nextftc.SequentialCommandGroup {
    return com.rowanmcalpin.nextftc.SequentialCommandGroup().apply(block)
}

/**
 * Returns a parallel command group. Should be used in the following format:
 * val myParallelCommandGroup: ParallelCommandGroup
 *     get() = parallel {
 *         +FirstCommand()
 *         +SecondCommand()
 *     }
 * @param block the list of commands in the group
 */
fun parallel(block: com.rowanmcalpin.nextftc.ParallelCommandGroup.() -> Unit): com.rowanmcalpin.nextftc.ParallelCommandGroup {
    return com.rowanmcalpin.nextftc.ParallelCommandGroup().apply(block)
}

/**
 * Represents a command that contains and manages other commands. Used by SequentialCommandGroup and
 * ParallelCommandGroup.
 */
abstract class CommandGroup: com.rowanmcalpin.nextftc.Command() {

    val commands: MutableList<com.rowanmcalpin.nextftc.Command> = mutableListOf()
    override val _isDone: Boolean
        get() = commands.isEmpty()

    /**
     * Allows people to add commands using myCommandGroup += MyCommand()
     * @param command the command being added
     */
    operator fun plusAssign(command: com.rowanmcalpin.nextftc.Command) {
        commands += command
    }

    /**
     * Makes the following format possible:
     * commandGroup {
     *     +FirstCommand()
     *     +SecondCommand()
     * }
     */
    operator fun com.rowanmcalpin.nextftc.Command.unaryPlus() = commands.add(this)

    /**
     * If this command group is ended forcefully, this function forcefully ends each of the commands
     * inside of it.
     */
    override fun end(interrupted: Boolean) {
        for (command in commands) {
            command.end(interrupted)
        }
    }
}

/**
 * Runs a group of commands one at a time.
 */
class SequentialCommandGroup: com.rowanmcalpin.nextftc.CommandGroup() {

    /**
     * Starts the first command in the list.
     */
    override fun start() {
        if (commands.isNotEmpty()) {
            commands[0].start()
        }
    }

    /**
     * Starts the first command in the list if it hasn't been started already, then executes it, and
     * checks to see if it's done. If it is done, it runs the command's end function and removes it
     * from the list. It then starts the next command in the list.
     */
    override fun execute() {
        if (commands.isNotEmpty()) {
            if (!commands[0].isStarted) {
                commands[0].start()
                commands[0].isStarted = true
            }
            commands[0].execute()
            if (commands[0].isDone) {
                commands[0].end(false)
                commands.removeFirst()
                if (commands.isNotEmpty())
                    commands[0].start()
            }
        }
    }
}

/**
 * Runs a group of commands all at the same time. Since the commands in CommandScheduler are already
 * run in parallel, the main functional use for ParallelCommandGroups is to place them inside
 * SequentialCommandGroups, like this:
 * val mySequentialCommandGroup: SequentialCommandGroup
 *     get() = sequential {
 *         +FirstCommand()
 *         +SecondCommand()
 *         +parallel {
 *             +FirstParallelCommand()
 *             +SecondParallelCommand()
 *         }
 *     }
 * The code above will run FirstCommand until it finishes, then SecondCommand until it finishes, and
 * then run both FirstParallelCommand and SecondParallelCommand at the same time until they are both
 * finished.
 */
class ParallelCommandGroup: com.rowanmcalpin.nextftc.CommandGroup() {
    private val commandsToCancel: MutableMap<com.rowanmcalpin.nextftc.Command, Boolean> = mutableMapOf()

    /**
     * Starts each command in the list.
     */
    override fun start() {
        for (command in commands) {
            command.start()
            command.isStarted = true
        }
    }

    /**
     * Starts each command in the list if it hasn't been started already, then runs its execute
     * function, then adds it to the commandsToCancel list if it's finished. The reason the commands
     * can't be cancelled right away is because removing an item from a list while iterating
     * through the list leads to all sorts of wacky hijinks.
     */
    override fun execute() {
        for (command in commands) {
            if (!command.isStarted) {
                command.start()
                command.isStarted = true
            }
            command.execute()
            if(command.isDone) {
                commandsToCancel += Pair(command, false)
            }
        }
        clearCommands()
    }

    /**
     * Clears the commands that need to be cancelled. The reason the commands can't be cancelled in
     * the execute function is because removing an item from a list while iterating through the list
     * leads to all sorts of wacky hijinks.
     */
    private fun clearCommands() {
        for (pair in commandsToCancel) {
            val command: com.rowanmcalpin.nextftc.Command = pair.key
            val interrupted: Boolean = pair.value
            command.end(interrupted)
            commands -= command
        }
        commandsToCancel.clear()
    }
}
