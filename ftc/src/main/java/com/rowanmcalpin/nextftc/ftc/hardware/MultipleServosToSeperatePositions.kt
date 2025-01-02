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

package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import kotlin.math.abs

/**
 * This command moves multiple servos each to a different target position
 *
 * @param servos the map of servos to move & their corresponding positions
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MultipleServosToSeperatePositions @JvmOverloads constructor(val servos: Map<Servo, Double>,
                               override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(servos: Map<Servo, Double>, subsystem: Subsystem): this(servos, setOf(subsystem))

    override val isDone: Boolean
        get() = true

    override fun start() {
        servos.forEach {
            it.key.position = it.value
        }
    }
}