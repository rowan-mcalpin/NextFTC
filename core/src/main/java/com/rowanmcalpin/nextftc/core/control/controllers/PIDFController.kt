package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.control.coefficients.PIDFCoefficients
import kotlin.math.abs

/**
 * This is a PIDF controller that does all of the calculations internally, so you can just set the
 * coefficients and target and call calculate(). It does the rest.
 * 
 * @param coefficients the coefficients for this PIDF controller
 */
open class PIDFController(val coefficients: PIDFCoefficients): Controller {

    /**
     * This is a PID controller that does all of the calculations internally, so you can just set 
     * the coefficients and target and call calculate(). It does the rest.
     * 
     * @param kP proportional constant
     * @param kI integral constant
     * @param kD derivative constant
     * @param kF feedforward constant
     */
    @JvmOverloads
    constructor(kP: Double = 0.005, kI: Double = 0.0, kD: Double = 0.0, kF: Double = 0.0):
            this(PIDFCoefficients(kP, kI, kD, kF))

    override var target: Double = 0.0
    
    override var setPointTolerance: Double = 0.0
    
    private var lastError = 0.0
    private var integralSum = 0.0
    
    private var lastTimestamp = 0.0
    
    override fun calculate(reference: Double): Double {
        val currentTimestamp = System.nanoTime() / 1E9
        if (lastTimestamp == 0.0) lastTimestamp = currentTimestamp
        
        val elapsedTime = currentTimestamp - lastTimestamp
        lastTimestamp = currentTimestamp
        
        val error = target - reference
        integralSum += error * elapsedTime
        val derivative = (error - lastError) / elapsedTime
        
        return (error * coefficients.kP) + (integralSum * coefficients.kI) + (derivative * coefficients.kD) + coefficients.kF
    }

    @Deprecated("Removed to shift to Controllable interface")
    /**
     * Whether this controller is within a certain distance of the [target].
     *
     * @param reference the current state of the item being controlled
     */
    fun atSetPoint(reference: Double): Boolean {
        if (abs(target - reference) <= setPointTolerance) {
            return true
        }
        
        return false
    }

    override fun reset() {
        lastTimestamp = 0.0
        integralSum = 0.0
        lastError = 0.0
    }
}