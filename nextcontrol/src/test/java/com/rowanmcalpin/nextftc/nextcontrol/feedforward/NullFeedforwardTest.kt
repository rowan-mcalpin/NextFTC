package com.rowanmcalpin.nextftc.nextcontrol.feedforward

import com.rowanmcalpin.nextftc.nextcontrol.utils.KineticState
import org.junit.Assert.*
import org.junit.Test

class NullFeedforwardTest {
    @Test
    fun `calculate returns 0 feedforward`() {
        // Arrange
        val feedforward = NullFeedforward()
        val reference = KineticState(1.0, 2.0, 3.0)

        // Act
        val actual = feedforward.calculate(reference)

        // Assert
        assertEquals(0.0, actual, 0.0)
    }

    @Test
    fun `calculate returns 0 feedforward even for NaN and infinity input`() {
        // Arrange
        val feedforward = NullFeedforward()
        val reference = KineticState(1.0, Double.POSITIVE_INFINITY, Double.NaN)

        // Act
        val actual = feedforward.calculate(reference)

        // Assert
        assertEquals(0.0, actual, 0.0)
    }
}