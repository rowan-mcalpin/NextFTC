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

import org.junit.Assert.*

import org.junit.Test

class PassThroughFilterTest {

    @Test
    fun `returns value passed in`() {
        // Arrange
        val filter = PassThroughFilter()

        val input = 10.0

        // Act
        val actual = filter.filter(input)

        // Assert
        assertEquals(input, actual, 0.0)
    }
}