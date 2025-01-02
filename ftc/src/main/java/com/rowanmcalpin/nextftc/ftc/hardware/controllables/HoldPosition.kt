package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.Controller
import kotlin.math.abs

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