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