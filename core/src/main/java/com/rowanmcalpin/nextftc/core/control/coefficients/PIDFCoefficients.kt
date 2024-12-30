package com.rowanmcalpin.nextftc.core.control.coefficients

/**
 * Coefficients for a PIDF controller.
 * 
 * @param kP proportional constant
 * @param kI integral constant
 * @param kD derivative constant
 * @param kF feedforward constant
 */
data class PIDFCoefficients @JvmOverloads constructor(var kP: Double = 0.005,
                            var kI: Double = 0.0,
                            var kD: Double = 0.0,
                            var kF: Double = 0.0)
