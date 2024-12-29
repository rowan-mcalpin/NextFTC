package com.rowanmcalpin.nextftc.core.control.coefficients

/**
 * Coefficients for a PID controller.
 * 
 * @param kP proportional constant
 * @param kI integral constant
 * @param kD derivative constant
 */
data class PIDCoefficients(var kP: Double, var kI: Double, var kD: Double)
