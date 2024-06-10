package com.rowanmcalpin.nextftc.command.groups

import com.rowanmcalpin.nextftc.command.Command

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
class ParallelCommandGroup: CommandGroup() {
    private val commandsToCancel: MutableMap<Command, Boolean> = mutableMapOf()

    /**
     * Starts each command in the list.
     */
    override fun onStart() {
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
    override fun onExecute() {
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
            val command: Command = pair.key
            val interrupted: Boolean = pair.value
            command.end(interrupted)
            commands -= command
        }
        commandsToCancel.clear()
    }
}

