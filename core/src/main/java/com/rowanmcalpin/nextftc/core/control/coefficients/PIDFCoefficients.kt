package com.rowanmcalpin.nextftc.core.control.coefficients

/**
 * Coefficients for a PIDF controller.
 * 
 * @param kP proportional constant
 * @param kI integral constant
 * @param kD derivative constant
 * @param kF feedforward constant
 */
data class PIDFCoefficients(var kP: Double, var kI: Double, var kD: Double, var kF: Double)
