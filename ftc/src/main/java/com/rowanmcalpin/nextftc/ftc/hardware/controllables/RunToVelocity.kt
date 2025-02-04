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
import com.rowanmcalpin.nextftc.core.control.controllers.Controller
import kotlin.math.abs

/**
 * This implements a PID controller to drive a motor to a specified target velocity. Note that this
 * command will spin a motor to a specific velocity, *and then depower the motor*. To have it hold
 * velocity, set the subsystem's default command to a [HoldVelocity] command.
 *
 * @param controllable the [Controllable] to control
 * @param targetVelocity the target velocity
 * @param controller the [Controller] to implement
 * @param subsystems the list of [Subsystem]s this command interacts with (should be whatever
 *                      subsystem holds this command)
 * @param outCondition will be evaluated every update, and the command will stop once it returns true
 */
class RunToVelocity @JvmOverloads constructor(val controllable: Controllable, val targetVelocity: Double,
    val controller: Controller, override val subsystems: Set<Subsystem> = setOf(),
    val outCondition: () -> Boolean = { abs(controllable.velocity)-targetVelocity < 10 }): Command() {

    @JvmOverloads
    constructor(controllable: Controllable,
                targetVelocity: Double,
                controller: Controller,
                subsystem: Subsystem,
                outCondition: () -> Boolean = { abs(controllable.velocity)-targetVelocity < 10 }): this(controllable, targetVelocity, controller, setOf(subsystem), outCondition)

    override val isDone: Boolean
        get() = outCondition.invoke()

    override fun start() {
        controller.target = targetVelocity
        controller.reset()
    }

    override fun update() {
        controllable.power = controller.calculate(controllable.velocity)
    }
}