package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.Controller
import kotlin.math.abs

/**
 * This implements a [Controller] to drive a [Controllable] to a specified target position
 */
class RunToPosition @JvmOverloads constructor(val controllable: Controllable, val target: Double, val controller: Controller,
    override val subsystems: Set<Subsystem> = setOf()): Command() {
    override val isDone: Boolean
        get() = controller.atTarget(controllable.currentPosition)

    override fun start() {
        controller.target = target
        controller.reset()
    }

    override fun update() {
        val calculatedPower = controller.calculate(controllable.currentPosition)
        if (abs(controllable.power - calculatedPower) > 0.01) {
            controllable.power = calculatedPower
        }
    }
}