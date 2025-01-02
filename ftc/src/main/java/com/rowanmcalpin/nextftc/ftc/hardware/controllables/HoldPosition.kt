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

package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.Controller

/**
 * This implements a [Controller] to hold a [Controllable] in its current position.
 *
 * @param controllable the [Controllable] to control
 * @param controller the [Controller] to implement
 * @param subsystems the list of subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class HoldPosition @JvmOverloads constructor(val controllable: Controllable, val controller: Controller,
                                              override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(controllable: Controllable, controller: Controller, subsystem: Subsystem): this(controllable, controller, setOf(subsystem))

    override val isDone = false

    override fun start() {
        controller.target = controllable.currentPosition
        controller.reset()
    }

    override fun update() {
        controllable.power = controller.calculate(controllable.currentPosition)
    }
}