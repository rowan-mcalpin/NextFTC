/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.rowanmcalpin.nextftc.ftc.OpModeData
import kotlin.math.abs

/**
 * Wrapper class for motors that implements controllable (and can therefore be used with RunToPosition
 * commands).
 */
class MotorEx(val motor: DcMotorEx): Controllable {

    constructor(name: String): this(OpModeData.hardwareMap.get(DcMotorEx::class.java, name))

    private var cachedPower = 0.0 // By default, motors are unpowered

    private var tickOffset = 0.0 // By default, we don't want to offset from the motor's raw ticks

    /**
     * The tolerance that must be surpassed in order to update the motors power. Defaults to 0.01.
     */
    var cacheTolerance = 0.01

    /**
     * Gives the unmodified raw tick value of the motor
     */
    val rawTicks: Double
        get() = motor.currentPosition.toDouble()

    /**
     * This returns the current position of the motor, accounting for any offsets created by manually
     * settings its currentPosition.
     *
     * Setting this value tells it that its current position is actually the position you've set the variable to,
     * which will get accounted for automatically when getting this value.
     */
    override var currentPosition: Double
        get() = (motor.currentPosition.toDouble() - tickOffset)
        set(value) { tickOffset = rawTicks - value }

    /**
     * Current velocity of the motor. Setter does nothing
     */
    override var velocity: Double
        get() = motor.velocity
        set(value) { }

    var direction: Direction
        get() = motor.direction
        set(value) { motor.direction = value }

    /**
     * Gets / sets the current power of the motor (automatically implements power caching)
     */
    override var power: Double
        get() = cachedPower
        set(value) {
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            if (abs(cachedPower - value) > cacheTolerance) {
                motor.power = value
                cachedPower = value
            }
        }

    fun reverse(): MotorEx {
        direction = Direction.REVERSE
        return this
    }

    fun resetEncoder() {
        currentPosition = 0.0
    }
}