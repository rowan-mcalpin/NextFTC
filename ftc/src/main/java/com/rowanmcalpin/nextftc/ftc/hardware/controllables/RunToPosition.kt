package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.Controller
import kotlin.math.abs

/**
 * This implements a [Controller] to drive a [Controllable] to a specified target position. When it
 * finishes, it will set the [Controllable]'s power to 0. To have it hold position, set the default
 * command to a [HoldPosition] command.
 */
class RunToPosition @JvmOverloads constructor(val controllable: Controllable, val target: Double, val controller: Controller,
    override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(controllable: Controllable, target: Double, controller: Controller, subsystem: Subsystem): this(controllable, target, controller, setOf(subsystem))

    override val isDone: Boolean
        get() = controller.atTarget(controllable.currentPosition)

    override fun start() {
        controller.target = target
        controller.reset()
    }

    override fun update() {
        controllable.power = controller.calculate(controllable.currentPosition)
    }

    override fun stop(interrupted: Boolean) {
        controllable.power = 0.0
    }
}