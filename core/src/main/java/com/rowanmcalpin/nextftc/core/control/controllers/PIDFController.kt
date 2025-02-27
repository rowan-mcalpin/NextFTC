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

package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.command.PIDBoundaryException
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.Feedforward
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.withSign

/**
 * PID controller with various feedforward components.
 *
 * @author Originally from Roadrunner 0.5, ported to Kotlin & NextFTC by Zach.Waffle
 */
open class PIDFController
/**
 * @param pid     traditional PID coefficients
 * @param kF      custom feedforward that depends on position
 */ @JvmOverloads constructor(
    var kP: Double,
    var kI: Double,
    var kD: Double,
    private val kF: Feedforward = StaticFeedforward(0.0),
    override var setPointTolerance: Double = 10.0
) : Controller {

    override var target: Double = 0.0

    private var measuredValue = 0.0
    private var minIntegral = -1.0
    private var maxIntegral = 1.0

    private var totalError = 0.0
    private var prevErrorVal = 0.0

    private var lastTimeStamp = 0.0
    private var period = 0.0

    override fun reset() {
        totalError = 0.0
        prevErrorVal = 0.0
        lastTimeStamp = 0.0
    }

    fun atTarget(): Boolean {
        return super.atTarget(measuredValue)
    }

    open fun calculate(pv: Double, target: Double): Double {
        this.target = target
        return calculate(pv)
    }

    fun clearTotalError() {
        totalError = 0.0
    }

    override fun calculate(pv: Double): Double {

        val currentTimestamp: Double = System.nanoTime() / 1E9
        if (lastTimeStamp == 0.0) {
            lastTimeStamp = currentTimestamp
        }
        period = currentTimestamp - lastTimeStamp
        lastTimeStamp = currentTimestamp

        val derivative = if (abs(period) > 1E-6) ((target - pv) - prevErrorVal) / period else 0.0

        totalError += period * (target - pv)
        totalError = if (totalError < minIntegral) minIntegral else min(maxIntegral, totalError)

        prevErrorVal = target - pv

        return kP * (target - pv) + kI * totalError + kD * derivative + kF.compute(target)
    }
}
