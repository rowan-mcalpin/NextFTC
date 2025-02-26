package com.rowanmcalpin.nextftc.core.units

data class Angle private constructor(override val value: Double): Quantity<Angle>() {
    companion object {
        private const val DEGREES_TO_RADIANS = Math.PI / 180
        private const val REVOLUTIONS_TO_RADIANS = 2 * Math.PI

        private fun fromUnit(value: Double, scalar: Double) = Angle(value * scalar)

        /**
         * Creates a new [Angle] from an angle in radians
         * @param radians the angle in radians
         */
        @JvmStatic fun fromRad(radians: Double) = Angle(radians)

        /**
         * Creates a new [Angle] from an angle in radians
         * @param radians the angle in radians
         */
        @JvmStatic fun fromRad(radians: Int) = fromRad(radians.toDouble())

        /**
         * Creates a new [Angle] from an angle in degrees
         * @param degrees the angle in degrees
         */
        @JvmStatic fun fromDeg(degrees: Double) = fromUnit(degrees, DEGREES_TO_RADIANS)

        /**
         * Creates a new [Angle] from an angle in degrees
         * @param degrees the angle in degrees
         */
        @JvmStatic fun fromDeg(degrees: Int) = fromDeg(degrees.toDouble())

        /**
         * Creates a new [Angle] from an angle in full revolutions
         * @param revolutions the angle in full revolutions
         */
        @JvmStatic fun fromRev(revolutions: Double) = fromUnit(revolutions, REVOLUTIONS_TO_RADIANS)

        /**
         * Creates a new [Angle] from an angle in full revolutions
         * @param revolutions the angle in full revolutions
         */
        @JvmStatic fun fromRev(revolutions: Int) = fromRev(revolutions.toDouble())

        fun wrapAngle0To2Pi(angle: Double) = ((angle % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI)
        fun wrapAnglePiToPi(angle: Double) = ((angle + Math.PI) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI) - Math.PI
    }

    /**
     * The value of the angle in radians
     */
    @JvmField val inRad = value

    /**
     * The value of the angle in degrees
     */
    @JvmField val inDeg = value / DEGREES_TO_RADIANS

    /**
     * The value of the angle in full revolutions
     */
    @JvmField val inRev = value / REVOLUTIONS_TO_RADIANS

    /**
     * A new [Angle] wrapped from 0 to 2pi
     */
    @get:JvmName("wrapped")
    val wrapped get() = Angle(wrapAngle0To2Pi(value))

    /**
     * A new [Angle] wrapped from -pi to pi
     */
    @get:JvmName("normalized")
    val normalized get() = Angle(wrapAnglePiToPi(value))

    override fun newInstance(value: Double): Angle = Angle(value)

    override fun toString(): String = "$value rad"
}

/**
 * Creates a new [Angle] from an angle in radians
 */
val Double.rad: Angle get() = Angle.fromRad(this)

/**
 * Creates a new [Angle] from an angle in radians
 */
val Int.rad: Angle get() = Angle.fromRad(this)

/**
 * Creates a new [Angle] from an angle in degrees
 */
val Double.deg: Angle get() = Angle.fromDeg(this)

/**
 * Creates a new [Angle] from an angle in degrees
 */
val Int.deg: Angle get() = Angle.fromDeg(this)

/**
 * Creates a new [Angle] from an angle in full revolutions
 */
val Double.rev: Angle get() = Angle.fromRev(this)

/**
 * Creates a new [Angle] from an angle in full revolutions
 */
val Int.rev: Angle get() = Angle.fromRev(this)
