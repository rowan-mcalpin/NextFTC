package com.rowanmcalpin.nextftc.core.control.controllers

import kotlin.math.abs

/**
 * Interface all controllers must inherit from.
 */
interface Controller {
    /**
     * The target for the reference to converge to.
     */
    var target: Double

    /**
     * The tolerance for being "at the target"
     */
    var setPointTolerance: Double

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

    /**
     * Whether the controller is within a tolerable distance of the target
     */
    fun atTarget(reference: Double): Boolean {
        if (abs(target - reference) <= setPointTolerance) return true
        return false
    }
}