package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.Controller
import kotlin.math.abs

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
        val calculatedPower = controller.calculate(controllable.velocity)
        if (abs(controllable.power - calculatedPower) > 0.01) {
            controllable.power = calculatedPower
        }
    }
}