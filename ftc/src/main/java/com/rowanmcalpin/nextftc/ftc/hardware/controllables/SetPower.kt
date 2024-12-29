package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * Sets a controllable to a specific power without any internal feedback
 *
 * @param controllable the [Controllable] to control
 * @param power the power to set the [Controllable] to
 * @param subsystems the [Subsystem]s this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class SetPower @JvmOverloads constructor(val controllable: Controllable, val power: Double,
               override val subsystems: Set<Subsystem> = setOf()): Command() {
    override val isDone = true

    override fun start() {
        controllable.power = power
    }
}