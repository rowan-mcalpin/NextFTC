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
package org.atomicrobotics3805.cflib.utilCommands

import com.qualcomm.robotcore.util.ElapsedTime
import org.atomicrobotics3805.cflib.Command
import org.atomicrobotics3805.cflib.subsystems.Subsystem

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
): Command() {

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