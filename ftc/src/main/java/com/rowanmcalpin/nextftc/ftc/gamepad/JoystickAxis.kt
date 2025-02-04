/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 *     Copyright (C) 2025 Rowan McAlpin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.rowanmcalpin.nextftc.ftc.gamepad

import kotlin.math.abs

/**
 * An axis of a joystick that has a value between -1 and 1.
 * @param axis the value of the axis to watch
 * @param threshold the amount the axis has to be moved in either direction before it is considered 
 * 'displaced'
 */
class JoystickAxis(private val axis: () -> Float, private val threshold: Float = 0f, private val reverse: Boolean): Control() {
    /**
     * The amount the joystick is being moved.
     */
    var value: Float = 0f
    
    override fun update() {
        value = axis() * if (reverse) -1 else 1
        updateState(abs(value) > threshold)
    }
}