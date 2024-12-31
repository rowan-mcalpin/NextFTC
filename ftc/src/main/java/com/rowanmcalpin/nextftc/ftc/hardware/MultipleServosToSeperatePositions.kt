package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import kotlin.math.abs

/**
 * This command moves multiple servos each to a different target position
 *
 * @param servos the map of servos to move & their corresponding positions
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MultipleServosToSeperatePositions @JvmOverloads constructor(val servos: Map<Servo, Double>,
                               override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(servos: Map<Servo, Double>, subsystem: Subsystem): this(servos, setOf(subsystem))

    override val isDone: Boolean
        get() = true

    override fun start() {
        servos.forEach {
            it.key.position = it.value
        }
    }
}