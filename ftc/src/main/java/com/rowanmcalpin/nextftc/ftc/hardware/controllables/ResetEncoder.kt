package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This command resets the encoder using a custom implementation (in other words, it does not change
 * the RunMode of the motor.
 */
class ResetEncoder @JvmOverloads constructor(val motor: MotorEx, override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(motor: MotorEx, subsystem: Subsystem): this(motor, setOf(subsystem))

    override val isDone: Boolean
        get() = true

    override fun start() {
        motor.resetEncoder()
    }
}