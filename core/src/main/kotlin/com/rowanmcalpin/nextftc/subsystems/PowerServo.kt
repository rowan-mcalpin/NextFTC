package com.rowanmcalpin.nextftc.subsystems

import com.qualcomm.robotcore.util.ElapsedTime
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.hardware.CRServoEx
import com.rowanmcalpin.nextftc.hardware.ServoEx
import kotlin.math.abs

/**
 * Powers a continuous rotation servo in a set direction & power
 *
 * @param servo the servo to move
 * @param power the position (-1.0 to 1.0) to move it (and in what direction)
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 */
@Suppress("unused")
class PowerServo(private val servo: CRServoEx,
                 private val power: Double,
                 override val requirements: List<Subsystem> = arrayListOf(),
                 override val interruptible: Boolean = true) : Command() {

    override val _isDone = true

    /**
     * Calculates the power to set the servo, and sets the servo to the calculated power.
     */
    override fun onStart() {
        servo.position = ((power * (if(servo.direction == CRServoEx.Direction.FORWARD) 1 else -1)) / 2) + 0.5
    }
}
