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

package com.rowanmcalpin.nextftc.ftc.gamepad

/**
 * This is the abstract class that all Controls extend. All controls have a state, and some
 * way to detect whether their state just became true or just became false.
 */
abstract class Control {
    /**
     * The current boolean state of this control.
     */
    var state: Boolean = false

    /**
     * Whether the current boolean state just changed to `true`.
     */
    var risingState: Boolean = false

    /**
     * Whether the current boolean state just changed to `false`.
     */
    var fallingState: Boolean = false

    /**
     * Whether the current boolean state just changed.
     */
    var stateChanged: Boolean = false

    /**
     * Updates the current state and the rising, falling, and changed states. Further, schedules
     * commands accordingly.
     */
    abstract fun update()

    /**
     * Updates whether the control state and whether it is rising or falling.
     */
    protected fun updateState(newState: Boolean) {
        risingState = newState && !state
        fallingState = !newState && state
        stateChanged = newState != state
        state = newState
    } 
}