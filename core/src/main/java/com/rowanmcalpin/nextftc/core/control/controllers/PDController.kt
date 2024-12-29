package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.control.coefficients.PDCoefficients
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients

/**
 * This is a PD controller that does all of the calculations internally, so you can just set the
 * coefficients and target and call calculate(). It does the rest.
 * 
 * @param coefficients the coefficients for this PD controller
 */
open class PDController(coefficients: PDCoefficients): PIDController(PIDCoefficients(coefficients.kP, 0.0, coefficients.kD))