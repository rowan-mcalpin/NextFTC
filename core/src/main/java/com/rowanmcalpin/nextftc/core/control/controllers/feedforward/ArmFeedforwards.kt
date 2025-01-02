package com.rowanmcalpin.nextftc.core.control.controllers.feedforward

import kotlin.math.cos

/**
 * ArmFeedforward is a simple implementation of [Feedforward] where the feedforward scales depending
 * on the cosine of the arm angle. This is ideal for an arm with a fixed length.
 *
 * @param kCos constant multiplier for output
 * @param ticksToAngle conversion function to convert input ticks into an angle (in radians)
 */
class ArmFeedforward(val kCos: Double, val ticksToAngle: (Double) -> Double): Feedforward {
    override fun compute(position: Double): Double = kCos * cos(ticksToAngle(position))
}

/**
 * GainScheduledArmFeedforward is very similar to [ArmFeedforward], but the multiplier is no longer
 * constant. This allows you to change your feedforward depending on, say, the extension of a linear
 * slide that rotates.
 *
 * @param kCos function to get the multiplier at a given position
 * @param ticksToAngle conversion function to convert input ticks into an angle (in radians)
 */
class GainScheduledArmFeedforward(val kCos: (Double) -> Double, val ticksToAngle: (Double) -> Double): Feedforward {
    override fun compute(position: Double): Double = kCos(position) * cos(ticksToAngle(position))
}