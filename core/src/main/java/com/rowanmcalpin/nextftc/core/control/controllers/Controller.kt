package com.rowanmcalpin.nextftc.core.control.controllers

/**
 * Interface all controllers must inherit from.
 */
interface Controller {
    /**
     * The target for the reference to converge to.
     */
    var target: Double

    /**
     * Given a reference, calculates how to best match the target.
     *
     * @param reference the current location of the motor being controlled
     */
    fun calculate(reference: Double): Double

    /**
     * Resets the control loop
     */
    fun reset()
}