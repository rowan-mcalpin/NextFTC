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

package com.rowanmcalpin.nextftc.nextcontrol.filters

/**
 * A simple low pass filter.
 *
 * High values of A are smoother but have more phase lag, low values of A allow more noise but
 * will respond faster to quick changes in the measured state.
 *
 * @param alpha The low pass gain (A). Must be between 0 and 1.
 */
class LowPassFilter @JvmOverloads constructor(val alpha: Double, var previousEstimate: Double = 0.0) : Filter {

    init {
        require(alpha in 0.0..1.0) { "Low pass gain must be between 0 and 1, but was $alpha" }
    }

    /**
     * Low Pass Filter estimate
     * @param sensorMeasurement unfiltered sensor reading
     * @return filtered estimate
     */
    override fun filter(sensorMeasurement: Double): Double {
        val estimate = alpha * previousEstimate + (1 - alpha) * sensorMeasurement
        previousEstimate = estimate
        return estimate
    }
}