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

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import kotlin.math.abs

@Deprecated("Removed in favor of controllables", replaceWith = ReplaceWith("RunToPosition"))
/**
 * This implements a PID controller to drive a motor to a specified target position.
 * 
 * @param motor the motor to control
 * @param target the target position
 * @param controller the controller to implement
 * @param subsystems the list of subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MotorToPosition(val motor: DcMotorEx, val target: Double, val controller: PIDFController,
                      override val subsystems: Set<Subsystem>): Command() {
    
    constructor(motor: DcMotorEx, target: Double, controller: PIDFController, subsystem: Subsystem): this(motor, target, controller, setOf(subsystem))
                          
    override val isDone: Boolean
        get() = controller.atTarget(motor.currentPosition.toDouble())

    override fun start() {
        controller.target = target
        controller.reset()
    }

    override fun update() {
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        val calculatedPower = controller.calculate(motor.currentPosition.toDouble())
        if (abs(motor.power - calculatedPower) > 0.001) {
            motor.power = calculatedPower
        }
    }
}