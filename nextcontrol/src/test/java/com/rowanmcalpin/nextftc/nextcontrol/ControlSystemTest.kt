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
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*

import org.junit.Test

class ControlSystemTest {

    @Test
    fun `delegates goal getter to interpolator`() {
        // Arrange
        val interpolator = mockk<InterpolatorElement>()
        val controlSystem = ControlSystem(mockk(), mockk(), mockk(), interpolator)

        val expected = KineticState(1.0, 2.0, 3.0)

        every { interpolator.goal } returns expected

        // Act
        val actual = controlSystem.goal

        // Assert
        assertEquals(expected, actual)
        verify (exactly = 1) { interpolator.goal }
    }

    @Test
    fun `delegates goal setter to interpolator`() {
        // Arrange
        val interpolator = mockk<InterpolatorElement>(relaxed = true)
        val controlSystem = ControlSystem(mockk(), mockk(), mockk(), interpolator)

        val input = KineticState(1.0, 2.0, 3.0)

        // Act
        controlSystem.goal = input

        // Assert
        verify (exactly = 1) { interpolator.goal = input }
    }

    @Test
    fun `uses default measurement when none is provided`() {
        // Arrange
        val filter = mockk<FilterElement>(relaxed = true)

        val controlSystem = ControlSystem(mockk(relaxed = true), mockk(relaxed = true), filter, mockk(relaxed = true))

        val defaultMeasurement = KineticState()

        // Act
        controlSystem.calculate()

        // Assert
        verify (exactly = 1) { filter.filter(defaultMeasurement) }
    }

    @Test
    fun `all elements are called`() {
        // Arrange
        val feedback = mockk<FeedbackElement>(relaxed = true)
        val feedforward = mockk<FeedforwardElement>(relaxed = true)
        val filter = mockk<FilterElement>(relaxed = true)
        val interpolator = mockk<InterpolatorElement>(relaxed = true)

        val controlSystem = ControlSystem(feedback, feedforward, filter, interpolator)

        // Act
        controlSystem.calculate()

        // Assert
        verify (exactly = 1) {
            feedback.calculate(any())
            feedforward.calculate(any())
            filter.filter(any())
        }
        verify (exactly = 2) {
            interpolator.currentReference
        }
    }

    @Test
    fun `error is interpolator reference minus filtered measurement`() {
        // Arrange
        val feedback = mockk<FeedbackElement>(relaxed = true)
        val filter = mockk<FilterElement>()
        val interpolator = mockk<InterpolatorElement>()

        val controlSystem = ControlSystem(feedback, mockk(relaxed = true), filter, interpolator)

        val filteredMeasurement = KineticState(1.0, 2.0, 3.0)
        every { filter.filter(any()) } returns filteredMeasurement

        val currentReference = KineticState(4.0, 5.0, 6.0)
        every { interpolator.currentReference } returns currentReference

        val expected = currentReference - filteredMeasurement

        // Act
        controlSystem.calculate()

        // Assert
        verify { feedback.calculate(expected) }
    }

    @Test
    fun `output is sum of feedback and feedforward`() {
        // Arrange
        val feedback = mockk<FeedbackElement>()
        val feedforward = mockk<FeedforwardElement>()

        every { feedback.calculate(any()) } returns 2.0
        every { feedforward.calculate(any()) } returns 3.0

        val controlSystem = ControlSystem(feedback, feedforward, mockk(relaxed = true), mockk(relaxed = true))

        // Act
        val result = controlSystem.calculate()

        // Assert
        assertEquals(5.0, result, 0.0)
    }

    @Test
    fun `uses provided sensor measurement`() {
        // Arrange
        val filter = mockk<FilterElement>(relaxed = true)
        val measurement = KineticState(1.0, 2.0, 3.0)

        val controlSystem = ControlSystem(mockk(relaxed = true), mockk(relaxed = true), filter, mockk(relaxed = true))

        // Act
        controlSystem.calculate(measurement)

        // Assert
        verify { filter.filter(measurement) }
    }
}