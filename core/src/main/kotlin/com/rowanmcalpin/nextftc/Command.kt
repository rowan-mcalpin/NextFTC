package com.rowanmcalpin.nextftc

import com.rowanmcalpin.nextftc.subsystems.Subsystem

/**
 * This class represents a command. These commands should be scheduled using
 * CommandScheduler.scheduleCommand(). Each command should represent one or more actions, and new
 * commands should not be scheduled every loop. The same instance of a command should not be
 * scheduled more than once.
 */
@Suppress("PropertyName")
abstract class Command {

    var isDone = false
        get() = field || _isDone
    open val _isDone = false
    var isStarted = false
    open val requirements: List<Subsystem> = arrayListOf()
    open val interruptible = true

    /**
     * This function is run repeatedly every loop. isDone is checked before this function is run.
     */
    open fun execute() { }

    /**
     * This function is run once at the beginning of the command. isDone is checked after this
     * function is run.
     */
    open fun start() { }

    /**
     * This function is run once at the end of the command, either when isDone is true, another
     * command using the same Subsystem is started, or the OpMode ends.
     * @param interrupted this is true if the command was interrupted by another command using the
     *                    same Subsystem or the OpMode ending. It's false if the command ended
     *                    naturally (i.e. because isDone returned true)
     */
    open fun end(interrupted: Boolean) { }
}