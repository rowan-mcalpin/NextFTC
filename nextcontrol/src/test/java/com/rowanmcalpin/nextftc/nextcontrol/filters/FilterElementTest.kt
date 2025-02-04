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

import com.rowanmcalpin.nextftc.nextcontrol.utils.KineticState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*

import org.junit.Test

class FilterElementTest {

    @Test
    fun `uses PassThroughFilter by default`() {
        // Arrange
        val filterElement = FilterElement()
        val input = KineticState(10.0, 20.0, 30.0)

        // Act
        val actual = filterElement.filter(input)

        // Assert
        assertEquals(input, actual)
    }

    @Test
    fun `uses custom position filter`() {
        // Arrange
        val positionFilter = mockk<Filter>()
        val filterElement = FilterElement(positionFilter = positionFilter)

        val input = KineticState(10.0, 20.0, 30.0)
        val expected = KineticState(1.0, 20.0, 30.0)

        every { positionFilter.filter(10.0) } returns 1.0

        // Act
        val actual = filterElement.filter(input)

        // Assert
        assertEquals(expected, actual)
        verify (exactly = 1) { positionFilter.filter(10.0) }
    }

    @Test
    fun `uses custom velocity filter`() {
        // Arrange
        val velocityFilter = mockk<Filter>()
        val filterElement = FilterElement(velocityFilter = velocityFilter)

        val input = KineticState(10.0, 20.0, 30.0)
        val expected = KineticState(10.0, 2.0, 30.0)

        every { velocityFilter.filter(20.0) } returns 2.0

        // Act
        val actual = filterElement.filter(input)

        // Assert
        assertEquals(expected, actual)
        verify (exactly = 1) { velocityFilter.filter(20.0) }
    }

    @Test
    fun `uses custom acceleration filter`() {
        // Arrange
        val accelerationFilter = mockk<Filter>()
        val filterElement = FilterElement(accelerationFilter = accelerationFilter)

        val input = KineticState(10.0, 20.0, 30.0)
        val expected = KineticState(10.0, 20.0, 3.0)

        every { accelerationFilter.filter(30.0) } returns 3.0

        // Act
        val actual = filterElement.filter(input)

        // Assert
        assertEquals(expected, actual)
        verify (exactly = 1) { accelerationFilter.filter(30.0) }
    }

    @Test
    fun `uses all three custom filters`() {
        // Arrange
        val positionFilter = mockk<Filter>()
        val velocityFilter = mockk<Filter>()
        val accelerationFilter = mockk<Filter>()
        val filterElement = FilterElement(
            positionFilter = positionFilter,
            velocityFilter = velocityFilter,
            accelerationFilter = accelerationFilter
        )

        val input = KineticState(10.0, 20.0, 30.0)
        val expected = KineticState(1.0, 2.0, 3.0)

        every { positionFilter.filter(10.0) } returns 1.0
        every { velocityFilter.filter(20.0) } returns 2.0
        every { accelerationFilter.filter(30.0) } returns 3.0

        // Act
        val actual = filterElement.filter(input)

        // Assert
        assertEquals(expected, actual)
        verify (exactly = 1) { positionFilter.filter(10.0) }
        verify (exactly = 1) { velocityFilter.filter(20.0) }
        verify (exactly = 1) { accelerationFilter.filter(30.0) }
    }
}