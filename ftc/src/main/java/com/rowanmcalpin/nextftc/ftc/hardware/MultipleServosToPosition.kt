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

package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This command moves multiple servos to a specified target position
 *
 * @param servos the list of servos to move
 * @param target the position to move the servos to
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MultipleServosToPosition @JvmOverloads constructor(val servos: List<Servo>, val target: Double,
                                override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(servos: List<Servo>, target: Double, subsystem: Subsystem): this(servos, target, setOf(subsystem))

    override val isDone: Boolean
        get() = true

    override fun start() {
        servos.forEach {
            it.position = target
        }
    }
}