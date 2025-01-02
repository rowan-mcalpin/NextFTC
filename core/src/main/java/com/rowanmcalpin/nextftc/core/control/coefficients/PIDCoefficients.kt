package com.rowanmcalpin.nextftc.core.control.coefficients

// these must be var so that tuning with dash works
data class PIDCoefficients(var kP: Double, var kI: Double, var kD: Double)