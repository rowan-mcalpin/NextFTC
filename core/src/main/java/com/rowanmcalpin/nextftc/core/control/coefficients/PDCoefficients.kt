package com.rowanmcalpin.nextftc.core.control.coefficients

/**
 * Coefficients for a PD controller.
 * 
 * @param kP proportional constant
 * @param kD derivative constant
 */
data class PDCoefficients(var kP: Double, var kD: Double)
