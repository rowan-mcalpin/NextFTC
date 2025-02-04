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

package com.rowanmcalpin.nextftc.nextcontrol

import com.rowanmcalpin.nextftc.nextcontrol.feedback.FeedbackElement
import com.rowanmcalpin.nextftc.nextcontrol.feedforward.FeedforwardElement
import com.rowanmcalpin.nextftc.nextcontrol.filters.FilterElement
import com.rowanmcalpin.nextftc.nextcontrol.interpolators.InterpolatorElement
import com.rowanmcalpin.nextftc.nextcontrol.utils.KineticState

class ControlSystem(
    private val feedback: FeedbackElement,
    private val feedforward: FeedforwardElement,
    private val filter: FilterElement,
    private val interpolator: InterpolatorElement,
) {
    var goal: KineticState by interpolator::goal

    @JvmOverloads
    fun calculate(sensorMeasurement: KineticState = KineticState()): Double {
        val filteredMeasurement = filter.filter(sensorMeasurement)

        val error = interpolator.currentReference - filteredMeasurement

        val feedbackOutput = feedback.calculate(error)
        val feedforwardOutput = feedforward.calculate(interpolator.currentReference)

        return feedbackOutput + feedforwardOutput
    }
}

