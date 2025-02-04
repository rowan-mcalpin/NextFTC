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

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

@Deprecated("Deprecated in favor of controllables", replaceWith = ReplaceWith("SetPower"))
/**
 * Sets a motor to a specific power without any internal feedback
 * 
 * @param motor the motor to control
 * @param power the power to set the motor to
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class BlindPowerMotor(val motor: DcMotorEx, val power: Double, override val subsystems: Set<Subsystem>): Command() {
    
    constructor(motor: DcMotorEx, power: Double, subsystem: Subsystem): this(motor, power, setOf(subsystem))
    
    override val isDone: Boolean
        get() = true

    override fun start() {
        motor.power = power
    }
}