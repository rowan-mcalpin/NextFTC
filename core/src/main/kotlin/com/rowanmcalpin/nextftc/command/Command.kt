package com.rowanmcalpin.nextftc.command

import com.rowanmcalpin.nextftc.CommandScheduler
import com.rowanmcalpin.nextftc.subsystems.Subsystem

/**
 * This class represents a command. These commands should be scheduled using
 * CommandScheduler.scheduleCommand(). Each command should represent one or more actions, and new
 * commands should not be scheduled every loop. The same instance of a command should not be
 * scheduled more than once.
 */
@Suppress("PropertyName")
abstract class Command {

    var isDone = true
        get() = field || _isDone
    open val _isDone = true
    var isStarted = false
    open val requirements: List<Subsystem> = arrayListOf()
    open val interruptible = true

    /**
     * This function is run repeatedly every loop. isDone is checked before this function is run.
     */
    fun execute() {
        onExecute()
    }

    /**
     * User-overridable function called repeatedly every loop.
     */
    open fun onExecute() { }

    /**
     * This function is run once at the beginning of the command. isDone is checked after this
     * function is run.
     */
    fun start() {
        onStart()
    }

    /**
     * User-overridable function called once when the command is first started.
     */
    open fun onStart() { }

    /**
     * This function is run once at the end of the command, either when isDone is true, another
     * command using the same Subsystem is started, or the OpMode ends.
     * @param interrupted this is true if the command was interrupted by another command using the
     *                    same Subsystem or the OpMode ending. It's false if the command ended
     *                    naturally (i.e. because isDone returned true)
     */
    fun end(interrupted: Boolean) {
        onEnd(interrupted)
    }

    /**
     * User-overridable function called once when the command ends.
     */
    open fun onEnd(interrupted: Boolean) { }

    /**
     * Invocation operator, which lets users call the command as if it's a function, and automatically schedule it.
     */
    operator fun invoke() {
        CommandScheduler.scheduleCommand(this)
    }
}