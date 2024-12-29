package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDFCoefficients

/**
 * This is a PID controller that does all of the calculations internally, so you can just set the
 * coefficients and target and call calculate(). It does the rest.
 * 
 * @param coefficients the coefficients for this PID controller
 */
open class PIDController(coefficients: PIDCoefficients): PIDFController(PIDFCoefficients(coefficients.kP, coefficients.kI, coefficients.kD, 0.0))