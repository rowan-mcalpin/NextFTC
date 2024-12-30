package com.rowanmcalpin.nextftc.core.control.controllers

import com.rowanmcalpin.nextftc.core.control.coefficients.PDCoefficients

/**
 * This is a proportional controller that does all of the calculations internally, so you can just 
 * set the coefficient and target and call calculate(). It does the rest.
 * 
 * @param kP the proportional constant for the proportional controller
 */
open class PController @JvmOverloads constructor(kP: Double = 0.005):
    PDController(PDCoefficients(kP, 0.0))