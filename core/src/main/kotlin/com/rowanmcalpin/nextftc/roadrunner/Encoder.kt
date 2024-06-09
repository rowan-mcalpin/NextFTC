/*
    Copyright (c) 2022 Atomic Robotics (https://atomicrobotics3805.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.rowanmcalpin.nextftc.roadrunner

import kotlin.jvm.JvmOverloads
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.acmerobotics.roadrunner.util.NanoClock

/**
 * Wraps a motor instance to provide corrected velocity counts and allow reversing without changing the corresponding
 * slot's motor direction
 */
class Encoder @JvmOverloads constructor(
    private val motor: DcMotorEx,
    private val clock: NanoClock = NanoClock.system()
) {
    enum class Direction(val multiplier: Int) {
        FORWARD(1), REVERSE(-1);

    }

    /**
     * Allows you to set the direction of the counts and velocity without modifying the motor's direction state
     * @param direction either reverse or forward depending on if encoder counts should be negated
     */
    var direction: Direction
    private var lastPosition: Int
    private var velocityEstimate: Double
    private var lastUpdateTime: Double

    init {
        direction = Direction.FORWARD
        lastPosition = 0
        velocityEstimate = 0.0
        lastUpdateTime = clock.seconds()
    }

    val currentPosition: Int
        get() {
            val multiplier = direction.multiplier
            val currentPosition = motor.currentPosition * multiplier
            if (currentPosition != lastPosition) {
                val currentTime = clock.seconds()
                val dt = currentTime - lastUpdateTime
                velocityEstimate = (currentPosition - lastPosition) / dt
                lastPosition = currentPosition
                lastUpdateTime = currentTime
            }
            return currentPosition
        }
    val rawVelocity: Double
        get() {
            val multiplier = direction.multiplier
            return motor.velocity * multiplier
        }
    val correctedVelocity: Double
        get() = inverseOverflow(rawVelocity, velocityEstimate)

    companion object {
        private const val CPS_STEP = 0x10000
        private fun inverseOverflow(input: Double, estimate: Double): Double {
            var real = input
            while (Math.abs(estimate - real) > CPS_STEP / 2.0) {
                real += Math.signum(estimate - real) * CPS_STEP
            }
            return real
        }
    }
}