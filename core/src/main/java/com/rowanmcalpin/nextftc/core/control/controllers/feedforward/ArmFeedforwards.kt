/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 *     Copyright (C) 2025 Rowan McAlpin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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