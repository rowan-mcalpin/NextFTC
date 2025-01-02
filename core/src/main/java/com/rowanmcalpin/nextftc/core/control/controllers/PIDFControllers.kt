package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.withSign

/**
 * Feedforward is a functional interface for computing feedforward.
 * It is essentially just a Function<Double, Double> but
 * named and with example uses.
 */
fun interface Feedforward {
    fun compute(position: Double): Double

    companion object {
        @JvmStatic fun static(kS: Double) = Feedforward { kS }
        @JvmStatic fun arm(kCos: Double) = Feedforward { kCos * cos(it) }
    }
}

/**
 * PID controller with various feedforward components.
 * Originally from Roadrunner 0.5
 * Ported to Kotlin and NextFTC by Zach.Waffle
 */
open class PIDFController
/**
 * @param pid     traditional PID coefficients
 * @param kF      custom feedforward that depends on position
 */ @JvmOverloads constructor(
    pid: PIDCoefficients,
    private val kF: Feedforward = Feedforward.static(0.0),
    override var target: Double = 0.0,
    override var setPointTolerance: Double = 10.0
) : Controller {

    constructor(
        kP: Double = 0.0,
        kI: Double = 0.0,
        kD: Double = 0.0,
        kF: Feedforward = Feedforward.static(0.0),
        target: Double = 0.0, 
        setPointTolerance: Double = 10.0
    ) : this(PIDCoefficients(kP, kI, kD), kF, target, setPointTolerance)

    var kP by pid::kP
    var kI by pid::kI
    var kD by pid::kD
     
    /**
     * Target position (that is, the controller setpoint).
     */

    /**
     * Target velocity.
     */
    var targetVelocity: Double = 0.0

    /**
     * Error computed in the last call to [.update]
     */
    var lastError: Double = 0.0
    private var errorSum = 0.0
    private var lastUpdateTs: Long = 0
    private var inputBounded = false
    private var minInput = 0.0
    private var maxInput = 0.0
    private var outputBounded = false
    private var minOutput = 0.0
    private var maxOutput = 0.0

    /**
     * Sets bound on the input of the controller. When computing the error, the min and max are
     * treated as the same value. (Imagine taking the segment of the real line between min and max
     * and attaching the endpoints.)
     *
     * @param min minimum input
     * @param max maximum input
     */
    fun setInputBounds(min: Double, max: Double) {
        assert(min < max) { "Min output must be less than max output!" }
        inputBounded = true
        minInput = min
        maxInput = max
    }

    /**
     * Sets bounds on the output of the controller.
     *
     * @param min minimum output
     * @param max maximum output
     */
    fun setOutputBounds(min: Double, max: Double) {
        assert(min < max) { "Min output must be less than max output!" }
        outputBounded = true
        minOutput = min
        maxOutput = max
    }


    fun getPositionError(measuredPosition: Double): Double {
        var error = target - measuredPosition
        if (inputBounded) {
            val inputRange = maxInput - minInput
            while (abs(error) > inputRange / .0) {
                error -= inputRange.withSign(error)
            }
        }
        return error
    }

    /**
     * Run a single iteration of the controller.
     *
     * @param timestamp        measurement timestamp as given by [System.nanoTime]
     * @param measuredPosition measured position (feedback)
     * @param measuredVelocity measured velocity
     */
    @JvmOverloads
    open fun calculate(
        timestamp: Long,
        measuredPosition: Double,
        measuredVelocity: Double? = null
    ): Double {
        val error = getPositionError(measuredPosition)

        if (lastUpdateTs == 0L) {
            lastError = error
            lastUpdateTs = timestamp
            return 0.0
        }

        val dt = (timestamp - lastUpdateTs).toDouble()
        errorSum += 0.5 * (error + lastError) * dt
        val errorDeriv = (error - lastError) / dt

        lastError = error
        lastUpdateTs = timestamp
        val velError = if (measuredVelocity == null) {
            errorDeriv
        } else {
            targetVelocity - measuredVelocity
        }

        val output =
            kP * error + kI * errorSum + kD * velError +
                    kF.compute(measuredPosition)

        if (outputBounded) {
            return max(minOutput, min(output, maxOutput))
        }

        return output
    }

    override fun calculate(
        reference: Double
    ): Double {
        return calculate(System.nanoTime(),reference)
    }

    /**
     * Reset the controller's integral sum.
     */
    override fun reset() {
        errorSum = 0.0
        lastError = 0.0
        lastUpdateTs = 0
    }

    @Deprecated("Use supertype method atTarget instead", ReplaceWith("atTarget(reference)"))
    fun atSetPoint(reference: Double) = atTarget(reference)
}

class PController(kP: Double, 
                  target: Double = 0.0,
                  setPointTolerance: Double = 10.0
) : PIDFController(kP=kP, target=target, setPointTolerance=setPointTolerance)

class PDController(kP: Double, 
    kD: Double,
    target: Double = 0.0,
    setPointTolerance: Double = 0.0
) : PIDFController(kP=kP, kD=kD, target=target, setPointTolerance=setPointTolerance)

class PIDController(kP: Double,
                     kI: Double,
                     kD: Double,
                     target: Double = 0.0,
                     setPointTolerance: Double = 0.0
) : PIDFController(kP=kP, kI=kI, kD=kD, target=target, setPointTolerance=setPointTolerance)