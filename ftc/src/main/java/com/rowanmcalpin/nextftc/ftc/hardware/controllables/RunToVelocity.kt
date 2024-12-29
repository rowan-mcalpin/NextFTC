package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.Controller
import kotlin.math.abs

/**
 * This implements a PID controller to drive a motor to a specified target velocity.
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
    override val isDone: Boolean
        get() = outCondition.invoke()

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