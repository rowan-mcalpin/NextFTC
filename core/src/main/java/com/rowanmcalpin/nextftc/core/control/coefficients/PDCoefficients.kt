package com.rowanmcalpin.nextftc.core.control.coefficients

/**
 * Coefficients for a PD controller.
 * 
 * @param kP proportional constant
 * @param kD derivative constant
 */
data class PDCoefficients @JvmOverloads constructor(var kP: Double = 0.005,
                                                    var kD: Double = 0.0)
