package com.rowanmcalpin.nextftc.command

import com.rowanmcalpin.nextftc.subsystems.Subsystem

/**
 * Represents a command, a discrete unit of functionality that runs simultaneous to all other commands every single
 * loop.
 */
@Suppress("PropertyName")
abstract class Command {
    /**
     * Whether the command is finished executing. Used to manually tell the command it's done via code.
     */
    var isDone = false
        get() = field || _isDone

    /**
     * Whether the command is finished executing. Should be overriden in *every* implementation of the [Command] class,
     * using a getter function.
     */
    open val _isDone = true

    /**
     * Internally used to track if the command has been started.
     */
    var isStarted = false

    /**
     * Subsystems that this command uses. If another command is running with an overlap, the other command is cancelled,
     * unless its [interruptible] value is false.
     */
    open val requirements: List<Subsystem> = arrayListOf()

    /**
     * Whether this command can be cancelled due to a subsystem conflict. This does not dictate whether the command can
     * be cancelled due to a stop request; stop requests always cancel all commands.
     */
    open val interruptible = true

    /**
     * Internally-called to repeat every loop.
     */
    fun execute() {
        onExecute()
    }

    /**
     * User-overridable function called automatically every loop.
     */
    open fun onExecute() { }

    /**
     * Internal function called once when the command is first started.
     */
    fun start() {
        isStarted = true
        onStart()
    }

    /**
     * User-overridable function called once automatically when the command is first started.
     */
    open fun onStart() { }

    /**
     * This function is run once at the end of the command, either when isDone is true, another
     * command using the same Subsystem is started, or the OpMode ends.
     * Internal function that is called automatically when the command is stopped.
     *
     * @param interrupted whether the command finished because [isDone] (or [_isDone]) was true, or if it was
     *                      interrupted due to a stop request or another command being scheduled.
     */
    fun end(interrupted: Boolean) {
        onEnd(interrupted)
    }

    /**
     * User-overridable function called once automatically when the command is ended.
     *
     * @param interrupted whether the command finished because [isDone] (or [_isDone]) was true, or if it was
     *                      interrupted due to a stop request or another command being scheduled.
     */
    open fun onEnd(interrupted: Boolean) { }

    /**
     * Invocation operator, which lets users call the command as if it's a function, and automatically schedule it.
     */
    operator fun invoke() {
        CommandScheduler.scheduleCommand(this)
    }
}