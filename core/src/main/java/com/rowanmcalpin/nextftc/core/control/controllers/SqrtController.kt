package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.Feedforward
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * This is a Square Root Controller, which is a proportional controller that uses the square root of
 * the error instead of just the error.
 *
 * @param pid PID coefficients
 * @param kF custom feedforward that depends on position
 */
class SqrtController @JvmOverloads constructor(
    pid: PIDCoefficients,
    kF: Feedforward = StaticFeedforward(0.0),
    target: Double = 0.0,
    setPointTolerance: Double = 10.0
) : PIDFController(pid=pid, kF=kF, target=target, setPointTolerance=setPointTolerance) {

    @JvmOverloads
    constructor(
        kP: Double = 0.0,
        kI: Double = 0.0,
        kD: Double = 0.0,
        kF: Feedforward = StaticFeedforward(0.0),
        target: Double = 0.0,
        setPointTolerance: Double = 10.0
    ) : this(PIDCoefficients(kP, kI, kD), kF, target, setPointTolerance)

    override fun calculate(
        timestamp: Long,
        measuredPosition: Double,
        measuredVelocity: Double?
    ): Double {
        val result = super.calculate(timestamp, measuredPosition, measuredVelocity)

        return sqrt(abs(result)) * sign(result)
    }

    override fun calculate(reference: Double): Double {
        return this.calculate(System.nanoTime(), reference)
    }
}

typealias SquidController = SqrtController