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

/**
 * This implements a PID controller to hold a motor at its current velocity. This command never
 * finishes, so it should be implemented as a DefaultCommand in your subsystem.
 *
 * @param controllable the [Controllable] to control
 * @param controller the [Controller] to implement
 * @param subsystems the list of [Subsystem]s this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class HoldVelocity @JvmOverloads constructor(val controllable: Controllable, val controller: Controller, override val subsystems: Set<Subsystem> = setOf()): Command() {

     constructor(controllable: Controllable,
                controller: Controller,
                subsystem: Subsystem): this(controllable, controller, setOf(subsystem))

    override val isDone = false

    private val targetVelocity = controllable.velocity

    override fun start() {
        controller.target = targetVelocity
        controller.reset()
    }

    override fun update() {
        controllable.power = controller.calculate(controllable.velocity)
    }
}