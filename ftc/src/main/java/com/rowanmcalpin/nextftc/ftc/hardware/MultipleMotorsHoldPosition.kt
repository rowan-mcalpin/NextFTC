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

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import kotlin.math.abs

@Deprecated("Removed in favor of controllables", replaceWith = ReplaceWith("RunToPosition"))
/**
 * This command holds a multiple motor's positions until another command is scheduled that uses the 
 * same subsystem.
 * 
 * @param motorsAndControllers a map of all motors and their corresponding controllers
 * @param subsystems the list of subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MultipleMotorsHoldPosition(val motorsAndControllers: Map<DcMotorEx, PIDFController>,
                                 override val subsystems: Set<Subsystem>): Command() {
                                     
    constructor(motorsAndControllers: Map<DcMotorEx, PIDFController>, subsystem: Subsystem): this(motorsAndControllers, setOf(subsystem))
    
    override val isDone = false

    override fun start() {
        if (motorsAndControllers.isEmpty()) {
            throw IllegalArgumentException("You must specify at least one pair of motors and controllers")
        }

        motorsAndControllers.forEach {
            it.value.target = it.key.currentPosition.toDouble()
            it.value.reset()
        }
    }

    override fun update() {
        motorsAndControllers.forEach {
            it.key.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            val calculatedPower = it.value.calculate(it.key.currentPosition.toDouble())
            if (abs(it.key.power - calculatedPower) > 0.001) {
                it.key.power = calculatedPower
            }
        }
    }
}