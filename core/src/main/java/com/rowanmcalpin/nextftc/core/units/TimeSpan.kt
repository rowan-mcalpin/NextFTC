package com.rowanmcalpin.nextftc.core.units

/**
 * A quantity of a time-span; a duration
 * @param value the value in milliseconds
 * @author BeepBot99
 */
data class TimeSpan(override val value: Double): Quantity<TimeSpan>() {
    companion object {
        private const val MILLISECONDS_TO_MICROSECONDS = 1e+3
        private const val SECONDS_TO_MICROSECONDS = 1e+6

        private fun fromUnit(value: Double, scalar: Double) = TimeSpan(value * scalar)

        /**
         * Creates a new [TimeSpan] from a time-span in microseconds
         * @param microseconds the time-span in microseconds
         */
        @JvmStatic fun fromUs(microseconds: Double) = TimeSpan(microseconds)

        /**
         * Creates a new [TimeSpan] from a time-span in microseconds
         * @param microseconds the time-span in microseconds
         */
        @JvmStatic fun fromUs(microseconds: Int) = fromUs(microseconds.toDouble())

        /**
         * Creates a new [TimeSpan] from a time-span in milliseconds
         * @param milliseconds the time-span in milliseconds
         */
        @JvmStatic fun fromMs(milliseconds: Double) = fromUnit(milliseconds, MILLISECONDS_TO_MICROSECONDS)

        /**
         * Creates a new [TimeSpan] from a time-span in milliseconds
         * @param milliseconds the time-span in milliseconds
         */
        @JvmStatic fun fromMs(milliseconds: Int) = fromMs(milliseconds.toDouble())

        /**
         * Creates a new [TimeSpan] from a time-span in seconds
         * @param seconds the time-span in seconds
         */
        @JvmStatic fun fromSec(seconds: Double) = fromUnit(seconds, SECONDS_TO_MICROSECONDS)

        /**
         * Creates a new [TimeSpan] from a time-span in seconds
         * @param seconds the time-span in seconds
         */
        @JvmStatic fun fromSec(seconds: Int) = fromSec(seconds.toDouble())

        @JvmStatic val ZERO = TimeSpan(0.0)

        @JvmStatic val NaN = TimeSpan(Double.NaN)

        @JvmStatic val POSITIVE_INFINITY = TimeSpan(Double.POSITIVE_INFINITY)

        @JvmStatic val NEGATIVE_INFINITY = TimeSpan(Double.NEGATIVE_INFINITY)
    }

    /**
     * The value of the time-span in microseconds
     */
    @JvmField val inUs = value

    /**
     * The value of the time-span in milliseconds
     */
    @JvmField val inMs = value / MILLISECONDS_TO_MICROSECONDS

    /**
     * The value of the time-span in seconds
     */
    @JvmField val inSec = value / SECONDS_TO_MICROSECONDS

    override fun newInstance(value: Double): TimeSpan = TimeSpan(value)

    override fun toString(): String = "$value us"
}

/**
 * Creates a new [TimeSpan] from a time-span in microseconds
 */
val Double.us: TimeSpan get() = TimeSpan.fromUs(this)

/**
 * Creates a new [TimeSpan] from a time-span in microseconds
 */
val Int.us: TimeSpan get() = TimeSpan.fromUs(this)

/**
 * Creates a new [TimeSpan] from a time-span in milliseconds
 */
val Double.ms: TimeSpan get() = TimeSpan.fromMs(this)

/**
 * Creates a new [TimeSpan] from a time-span in milliseconds
 */
val Int.ms: TimeSpan get() = TimeSpan.fromMs(this)

/**
 * Creates a new [TimeSpan] from a time-span in seconds
 */
val Double.sec: TimeSpan get() = TimeSpan.fromSec(this)

/**
 * Creates a new [TimeSpan] from a time-span in seconds
 */
val Int.sec: TimeSpan get() = TimeSpan.fromSec(this)
