package com.rowanmcalpin.nextftc.nextcontrol.feedback

import com.rowanmcalpin.nextftc.nextcontrol.feedforward.NullFeedforward
import com.rowanmcalpin.nextftc.nextcontrol.utils.KineticState
import org.junit.Assert.*
import org.junit.Test

class NullFeedbackTest {
    @Test
    fun `calculate returns 0 feedback`() {
        // Arrange
        val feedback = NullFeedback()
        val error = KineticState(1.0, 2.0, 3.0)

        // Act
        val actual = feedback.calculate(error)

        // Assert
        assertEquals(0.0, actual, 0.0)
    }

    @Test
    fun `calculate returns 0 feedback even for NaN and infinity input`() {
        // Arrange
        val feedback = NullFeedforward()
        val error = KineticState(1.0, Double.POSITIVE_INFINITY, Double.NaN)

        // Act
        val actual = feedback.calculate(error)

        // Assert
        assertEquals(0.0, actual, 0.0)
    }
}