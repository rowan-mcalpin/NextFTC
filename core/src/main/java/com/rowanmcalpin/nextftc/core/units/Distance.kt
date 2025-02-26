package com.rowanmcalpin.nextftc.core.units

/**
 * A quantity of distance or length
 * @param value the distance in millimeters
 * @author BeepBot99
 */
data class Distance private constructor(override val value: Double): Quantity<Distance>() {
    companion object {
        private const val CENTIMETERS_TO_MILLIMETERS = 10.0
        private const val METERS_TO_MILLIMETERS = 1000.0
        private const val INCHES_TO_MILLIMETERS = 25.4
        private const val FEET_TO_MILLIMETERS = 304.8
        private const val YARDS_TO_MILLIMETERS = 914.4

        private fun fromUnit(value: Double, scalar: Double) = Distance(value * scalar)

        /**
         * Creates a new [Distance] from a distance in millimeters
         * @param millimeters the distance in millimeters
         */
        @JvmStatic fun fromMm(millimeters: Double) = Distance(millimeters)

        /**
         * Creates a new [Distance] from a distance in millimeters
         * @param millimeters the distance in millimeters
         */
        @JvmStatic fun fromMm(millimeters: Int) = fromMm(millimeters.toDouble())

        /**
         * Creates a new [Distance] from a distance in centimeters
         * @param centimeters the distance in centimeters
         */
        @JvmStatic fun fromCm(centimeters: Double) = fromUnit(centimeters, CENTIMETERS_TO_MILLIMETERS)

        /**
         * Creates a new [Distance] from a distance in centimeters
         * @param centimeters the distance in centimeters
         */
        @JvmStatic fun fromCm(centimeters: Int) = fromCm(centimeters.toDouble())

        /**
         * Creates a new [Distance] from a distance in meters
         * @param meters the distance in meters
         */
        @JvmStatic fun fromMeters(meters: Double) = fromUnit(meters, METERS_TO_MILLIMETERS)

        /**
         * Creates a new [Distance] from a distance in meters
         * @param meters the distance in meters
         */
        @JvmStatic fun fromMeters(meters: Int) = fromMeters(meters.toDouble())

        /**
         * Creates a new [Distance] from a distance in inches
         * @param inches the distance in inches
         */
        @JvmStatic fun fromIn(inches: Double) = fromUnit(inches, INCHES_TO_MILLIMETERS)

        /**
         * Creates a new [Distance] from a distance in inches
         * @param inches the distance in inches
         */
        @JvmStatic fun fromIn(inches: Int) = fromIn(inches.toDouble())

        /**
         * Creates a new [Distance] from a distance in feet
         * @param feet the distance in feet
         */
        @JvmStatic fun fromFt(feet: Double) = fromUnit(feet, FEET_TO_MILLIMETERS)

        /**
         * Creates a new [Distance] from a distance in feet
         * @param feet the distance in feet
         */
        @JvmStatic fun fromFt(feet: Int) = fromFt(feet.toDouble())

        /**
         * Creates a new [Distance] from a distance in yards
         * @param yards the distance in yards
         */
        @JvmStatic fun fromYd(yards: Double) = fromUnit(yards, YARDS_TO_MILLIMETERS)

        /**
         * Creates a new [Distance] from a distance in yards
         * @param yards the distance in yards
         */
        @JvmStatic fun fromYd(yards: Int) = fromYd(yards.toDouble())
    }

    /**
     * The value of the distance in millimeters
     */
    @JvmField val inMm = value

    /**
     * The value of the distance in centimeters
     */
    @JvmField val inCm = value / CENTIMETERS_TO_MILLIMETERS

    /**
     * The value of the distance in meters
     */
    @JvmField val inMeters = value / METERS_TO_MILLIMETERS

    /**
     * The value of the distance in inches
     */
    @JvmField val inIn = value / INCHES_TO_MILLIMETERS

    /**
     * The value of the distance in feet
     */
    @JvmField val inFt = value / FEET_TO_MILLIMETERS

    /**
     * The value of the distance in yards
     */
    @JvmField val inYd = value / YARDS_TO_MILLIMETERS

    /**
     * Creates a new instance of [Distance] with the given value
     * @param value the value in millimeters to create an instance with
     */
    override fun newInstance(value: Double): Distance = Distance(value)

    override fun toString(): String = "$value mm"
}

/**
 * Creates a new [Distance] from a distance in millimeters
 */
val Double.mm: Distance get() = Distance.fromMm(this)

/**
 * Creates a new [Distance] from a distance in millimeters
 */
val Int.mm: Distance get() = Distance.fromMm(this)

/**
 * Creates a new [Distance] from a distance in centimeters
 */
val Double.cm: Distance get() = Distance.fromCm(this)

/**
 * Creates a new [Distance] from a distance in centimeters
 */
val Int.cm: Distance get() = Distance.fromCm(this)

/**
 * Creates a new [Distance] from a distance in meters
 */
val Double.m: Distance get() = Distance.fromMeters(this)

/**
 * Creates a new [Distance] from a distance in meters
 */
val Int.m: Distance get() = Distance.fromMeters(this)

/**
 * Creates a new [Distance] from a distance in inches
 */
val Double.inches: Distance get() = Distance.fromIn(this)

/**
 * Creates a new [Distance] from a distance in inches
 */
val Int.inches: Distance get() = Distance.fromIn(this)

/**
 * Creates a new [Distance] from a distance in inches
 */
val Double.inch: Distance get() = Distance.fromIn(this)

/**
 * Creates a new [Distance] from a distance in inches
 */
val Int.inch: Distance get() = Distance.fromIn(this)

/**
 * Creates a new [Distance] from a distance in feet
 */
val Double.ft: Distance get() = Distance.fromFt(this)

/**
 * Creates a new [Distance] from a distance in feet
 */
val Int.ft: Distance get() = Distance.fromFt(this)

/**
 * Creates a new [Distance] from a distance in yards
 */
val Double.yd: Distance get() = Distance.fromYd(this)

/**
 * Creates a new [Distance] from a distance in yards
 */
val Int.yd: Distance get() = Distance.fromYd(this)
