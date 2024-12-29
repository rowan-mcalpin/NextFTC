package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This command moves a servo to a target position
 * 
 * @param servo the servo to move
 * @param targetPosition the position to move the servo to
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class ServoToPosition(val servo: Servo, val targetPosition: Double,
                      override val subsystems: Set<Subsystem>): Command() {
                          
    constructor(servo: Servo, targetPosition: Double, subsystem: Subsystem): this(servo, targetPosition, setOf(subsystem))
                          
    override val isDone: Boolean
        get() = true

    override fun start() {
        servo.position = targetPosition
    }
}