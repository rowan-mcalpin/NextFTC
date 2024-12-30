package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import kotlin.math.abs

/**
 * This command moves multiple servos to a specified target position
 *
 * @param servos the list of servos to move
 * @param target the position to move the servos to
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MultipleServosToPosition(val servos: List<Servo>, val target: Double,
                                override val subsystems: Set<Subsystem>): Command() {

    constructor(servos: List<Servo>, target: Double, subsystem: Subsystem): this(servos, target, setOf(subsystem))

    override val isDone: Boolean
        get() = true

    override fun start() {
        servos.forEach {
            if (abs(it.position - target) > 0.01) {
                it.position = target
            }
        }
    }
}