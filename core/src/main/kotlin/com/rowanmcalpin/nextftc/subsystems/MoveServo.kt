package com.rowanmcalpin.nextftc.subsystems

import com.qualcomm.robotcore.util.ElapsedTime
import com.rowanmcalpin.nextftc.Command
import com.rowanmcalpin.nextftc.hardware.ServoEx
import kotlin.math.abs

/**
 * Moves a given servo to a position and waits for some amount of time until it gets there. The time is calculated by
 * finding the difference in position between where it was just set to and where it was last set to, and multiplying it
 * by maxTime.
 *
 * @param servo the servo to move
 * @param position the position (0.0 to 1.0) to move it to
 * @param maxTime the time in seconds to move the servo from 0.0 position to 1.0
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 */
@Suppress("unused")
class MoveServo(private val servo: ServoEx,
                private val position: Double,
                private val maxTime: Double,
                override val requirements: List<Subsystem> = arrayListOf(),
                override val interruptible: Boolean = true) : Command() {

    private var positionDif = 0.0
    private val timer = ElapsedTime()
    override val _isDone: Boolean
        get() = timer.seconds() > positionDif * maxTime

    /**
     * Calculates the difference in position, moves the servo, and resets the timer
     */
    override fun start() {
        positionDif = abs(position - servo.position)
        if(positionDif == 0.0) {
            positionDif = 1.0
        }
        servo.position = position
        timer.reset()
    }
}
