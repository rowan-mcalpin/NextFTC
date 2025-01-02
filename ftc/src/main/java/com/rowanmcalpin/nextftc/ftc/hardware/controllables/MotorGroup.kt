package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import kotlin.math.abs

/**
 * A MotorGroup is a collection of [MotorEx]s that are all controlled by a single encoder (connected
 * to the leader motor)
 *
 * @param leader the [MotorEx] with the encoder that will be used
 * @param followers any other motors to control
 */
class MotorGroup(val leader: MotorEx, vararg val followers: MotorEx): Controllable {

    constructor(leaderName: String, vararg names: String): this(
        MotorEx(leaderName),
        *createMotors(names)
    )

    override var currentPosition: Double
        get() = leader.currentPosition
        set(value) { leader.currentPosition = value }

    override var velocity: Double
        get() = leader.velocity
        set(value) { }

    override var power: Double
        get() = leader.power
        set(value) {
            leader.power = value
            followers.forEach {
                it.power = value
            }
        }

    companion object {
        fun createMotors(names: Array<out String>): Array<out MotorEx> {
            val list = mutableListOf<MotorEx>()

            for (i in 0..names.size) {
                list += MotorEx(names[i])
            }

            return list.toTypedArray()
        }
    }
}