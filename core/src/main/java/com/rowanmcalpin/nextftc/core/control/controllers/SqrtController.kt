package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

class SqrtController @JvmOverloads constructor(
    pid: PIDCoefficients,
    kF: Feedforward = Feedforward.static(0.0),
    target: Double = 0.0,
    setPointTolerance: Double = 10.0
) : PIDFController(pid=pid, kF=kF, target=target, setPointTolerance=setPointTolerance) {

    constructor(
        kP: Double = 0.0,
        kI: Double = 0.0,
        kD: Double = 0.0,
        kF: Feedforward = Feedforward.static(0.0),
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