package com.rowanmcalpin.nextftc.command.groups

import com.rowanmcalpin.nextftc.command.Command

/**
 * Represents a command that contains and manages other commands. Used by SequentialCommandGroup and
 * ParallelCommandGroup.
 */
abstract class CommandGroup: Command() {

    val commands: MutableList<Command> = mutableListOf()
    override val _isDone: Boolean
        get() = commands.isEmpty()

    fun add(command: Command) {
        commands.add(command)
    }

    fun remove(command: Command) {
        if(commands.contains(command)) {
            commands.remove(command)
        }
    }

    /**
     * If this command group is ended forcefully, this function forcefully ends each of the commands
     * inside of it.
     */
    override fun onEnd(interrupted: Boolean) {
        for (command in commands) {
            command.end(interrupted)
        }
    }
}
