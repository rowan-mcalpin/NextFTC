package com.rowanmcalpin.nextftc.core.command.utility.statemachine

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.core.command.StateNotSetException
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand

/**
 * A command that acts as a finite state-machine
 *
 * Note that states must be added *before* the command is scheduled.
 * @param T the type of the state that will be used
 * @throws StateNotSetException if the command is scheduled before a state has been added
 * @author BeepBot99
 */
open class StateMachineCommand<T>: Command() {
    private val stateCommandMap: MutableMap<T, Command> = mutableMapOf()

    protected var currentState: T? = null
        set(value) {
            if (value in stateCommandMap.keys)
                field = value
            else
                throw IllegalArgumentException("state must be added to the state machine with .state() before using")
        }
    private var previousState: T? = null
    private lateinit var currentCommand: Command

    override var isDone: Boolean = false

    /**
     * Adds a new state to the state machine
     *
     * Is given the [StateMachineCommand] so that it can call [end] and [setState], if needed.
     * @param state the state to set
     * @param commandFactory a factory that returns a command to run when the current state is the set state
     * @return the [StateMachineCommand] for use with a Fluent API
     */
    fun state(state: T, commandFactory: (StateMachineCommand<T>) -> Command): StateMachineCommand<T> {
        currentState = currentState ?: state
        stateCommandMap[state] = commandFactory(this)
        return this
    }

    /**
     * Adds a new state to the state machine
     * @param state the state to set
     * @param command the command to run when the current state is the set state
     * @return the [StateMachineCommand] for use with a Fluent API
     */
    fun state(state: T, command: Command): StateMachineCommand<T> {
        return state(state) { command }
    }

    override fun start() {
        require(currentState != null) { throw StateNotSetException() }
        previousState = currentState
        currentCommand = stateCommandMap[currentState]!!
        CommandManager.scheduleCommand(currentCommand)
    }

    override fun update() {
        if (previousState == currentState) return

        CommandManager.cancelCommand(currentCommand)
        currentCommand = stateCommandMap[currentState]!!
        CommandManager.scheduleCommand(currentCommand)

        previousState = currentState
    }

    override fun stop(interrupted: Boolean) {
        CommandManager.cancelCommand(currentCommand)
    }

    /**
     * Stops the state machine
     */
    fun end() = InstantCommand({ isDone = true })

    /**
     * Sets the state of the state machine
     * @param state a factory that returns the state to set
     * @return a [Command] that sets the state and ends instantly
     */
    fun setState(state: () -> T) = InstantCommand({ currentState = state() })
}