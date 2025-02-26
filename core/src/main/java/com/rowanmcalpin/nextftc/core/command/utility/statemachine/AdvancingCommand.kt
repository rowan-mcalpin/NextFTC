package com.rowanmcalpin.nextftc.core.command.utility.statemachine

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.StateNotSetException

/**
 * A [Command] that advances through a list of commands
 * @throws StateNotSetException if the command returned by [advance] or [retreat] is called before a state has been added
 * @author BeepBot99
 */
class AdvancingCommand: StateMachineCommand<Int>() {
    private var states: Int = 0

    /**
     * Adds a new command to the advancing command
     * @param command the [Command] to add
     * @return the [AdvancingCommand] for use with a Fluent API
     */
    fun add(command: Command): AdvancingCommand {
        return state(states++, command) as AdvancingCommand
    }

    /**
     * Advances to the next command, looping back if it's on the last command
     * @return a [Command] that advances and ends instantly
     */
    fun advance(): Command {
        require(states != 0 && currentState != null) { StateNotSetException() }
        return setState { (currentState!! + 1) % states  }
    }

    /**
     * Retreats to the previous command, looping back if it's on the first command
     * @return a [Command] that retreats and ends instantly
     */
    fun retreat(): Command {
        require(states != 0 && currentState != null) { StateNotSetException() }
        return setState { (currentState!! - 1 + states) % states }
    }
}