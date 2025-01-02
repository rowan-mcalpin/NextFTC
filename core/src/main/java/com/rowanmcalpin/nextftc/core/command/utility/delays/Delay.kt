/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rowanmcalpin.nextftc.core.command.utility.delays

import com.rowanmcalpin.nextftc.core.command.Command

/**
 * A [Command] that does nothing except wait until a certain amount of time has passed. Like all 
 * delays, if placed directly in a sequential group, it will accomplish nothing except slowing loop 
 * times and taking up memory.
 * @param time the desired duration of this command, in seconds
 */
class Delay(
    private val time: Double = 0.0
): Command() {
    
    private var startTime: Double = 0.0
    
    override val isDone: Boolean
        get() = (System.nanoTime() / 1E9) - startTime >= time

    override fun start() {
        startTime = System.nanoTime().toDouble() / 1E9
    }
}