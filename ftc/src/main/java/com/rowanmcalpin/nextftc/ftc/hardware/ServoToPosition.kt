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
 * This command moves a servo to a target position
 * 
 * @param servo the servo to move
 * @param targetPosition the position to move the servo to
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class ServoToPosition(val servo: Servo, val targetPosition: Double,
                      override val subsystems: Set<Subsystem>): Command() {
                          
    constructor(servo: Servo, targetPosition: Double, subsystem: Subsystem): this(servo, targetPosition, setOf(subsystem))
                          
    override val isDone: Boolean
        get() = true

    override fun start() {
        servo.position = targetPosition
    }
}