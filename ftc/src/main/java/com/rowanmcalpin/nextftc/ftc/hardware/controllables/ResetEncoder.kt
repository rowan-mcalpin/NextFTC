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

package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This command resets the encoder using a custom implementation (in other words, it does not change
 * the RunMode of the motor.
 */
class ResetEncoder @JvmOverloads constructor(val motor: MotorEx, override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(motor: MotorEx, subsystem: Subsystem): this(motor, setOf(subsystem))

    override val isDone: Boolean
        get() = true

    override fun start() {
        motor.resetEncoder()
    }
}