package com.rowanmcalpin.nextftc.core.control.controllers.feedforward

/**
 * Feedforward is a functional interface for computing feedforward.
 * It is essentially just a Function<Double, Double> but
 * named and with example uses.
 */
fun interface Feedforward {
    fun compute(position: Double): Double
}