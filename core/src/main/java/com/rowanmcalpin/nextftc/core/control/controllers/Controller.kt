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

package com.rowanmcalpin.nextftc.core.control.controllers

import kotlin.math.abs

/**
 * Interface all controllers must inherit from.
 */
interface Controller {
    /**
     * The target for the reference to converge to.
     */
    var target: Double

    /**
     * The tolerance for being "at the target"
     */
    var setPointTolerance: Double

    /**
     * Given a reference, calculates how to best match the target.
     *
     * @param reference the current state of the item being controlled
     */
    fun calculate(reference: Double): Double

    /**
     * Given an error, calculates how to best match the target. Only uses PID, no feedforward.
     *
     * @param error the current error of the item being controlled
     */
    fun calculateFromError(error: Double): Double

    /**
     * Resets the control loop
     */
    fun reset()

    /**
     * Whether the controller is within a tolerable distance of the target
     */
    fun atTarget(reference: Double): Boolean {
        if (abs(target - reference) <= setPointTolerance) return true
        return false
    }
}