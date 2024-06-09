package com.rowanmcalpin.nextftc.utilCommands

import com.qualcomm.robotcore.util.ElapsedTime
import com.rowanmcalpin.nextftc.Command
import com.rowanmcalpin.nextftc.subsystems.Subsystem

/**
 * Sometimes, you may want to use a very simple command and don't want to create a whole class for
 * it. To avoid that, you can use this class and pass in any necessary functionality as constructor
 * parameters.
 * @param getDone equivalent of _isDone, determines whether the command is done. Always true by
 *                default, meaning that if this isn't set, the command will run start() and then
 *                finish immediately
 * @param _execute equivalent of execute(), run every loop, does nothing by default
 * @param _start equivalent of start(), runs once at the command's start, does nothing by default
 * @param _done equivalent of end(), runs once at the command's end, does nothing by default
 * @param endTime the amount of time required for the command to finish (in addition to getDone
 *                being true). This is 0.0 by default, meaning that unless you set this, getDone is
 *                the only thing used to determine if the command is finished
 * @param requirements a list of subsystems used by this command
 * @param interruptible whether or not this command can be interrupted by other commands using the
 *                      same subsystem(s)
 */
open class CustomCommand(
    private val getDone: () -> Boolean = { true },
    private val _execute: () -> Unit = { },
    private val _start: () -> Unit = { },
    private val _done: (interrupted: Boolean) -> Unit = { },
    private val endTime: Double = 0.0,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true
): com.rowanmcalpin.nextftc.Command() {

    private val timer = ElapsedTime()
    override val _isDone: Boolean
        get() = getDone.invoke() && timer.seconds() > endTime

    /**
     * Resets the timer and invokes the _start lambda
     */
    override fun start() {
        timer.reset()
        _start.invoke()
    }

    /**
     * Invokes the _execute lambda
     */
    override fun execute() {
        _execute.invoke()
    }

    /**
     * Invokes the _end lambda
     */
    override fun end(interrupted: Boolean) {
        _done.invoke(interrupted)
    }
}