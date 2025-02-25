package com.rowanmcalpin.nextftc.core.units

import kotlin.math.abs

/**
 * Represents a physical quantity
 * @param T the type of the quantity
 * @author BeepBot99
 */
abstract class Quantity<T: Quantity<T>> {
    /**
     * The value of the quantity
     */
    abstract val value: Double

    operator fun plus(other: T): T = newInstance(value + other.value)
    operator fun minus(other: T): T = newInstance(value - other.value)
    operator fun times(other: T): T = newInstance(value * other.value)
    operator fun times(scalar: Double): T = newInstance(value * scalar)
    operator fun times(scalar: Int): T = newInstance(value * scalar)
    operator fun div(other: T): T = newInstance(value / other.value)
    operator fun div(scalar: Double): T = newInstance(value / scalar)
    operator fun div(scalar: Int): T = newInstance(value / scalar)
    operator fun unaryPlus(): T = newInstance(value)
    operator fun unaryMinus(): T = newInstance(-value)
    operator fun rem(other: T): T = newInstance(value % other.value)
    operator fun rem(divisor: Double): T = newInstance(value % divisor)
    operator fun compareTo(other: T): Int = value.compareTo(other.value)

    val sign: Int get() = when {
        value > 0 -> 1
        value < 0 -> -1
        else -> 0
    }

    @get:JvmName("abs")
    val abs: T get() = newInstance(abs(value))

    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int
    abstract override fun toString(): String

    /**
     * @return if [value] is NaN
     */
    fun isNaN(): Boolean = value.isNaN()

    /**
     * Creates a new instance of the class with the given value
     * @param value the value to create an instance with
     */
    abstract fun newInstance(value: Double): T
}