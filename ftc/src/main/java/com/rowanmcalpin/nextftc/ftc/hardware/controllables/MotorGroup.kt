package com.rowanmcalpin.nextftc.ftc.hardware.controllables

/**
 * A MotorGroup is a collection of [MotorEx]s that are all controlled by a single encoder (connected
 * to the leader motor)
 *
 * @param leader the [MotorEx] with the encoder that will be used
 * @param followers any other motors to control
 */
class MotorGroup(val leader: MotorEx, vararg val followers: MotorEx): Controllable {

    constructor(vararg names: String): this(
        MotorEx(names[0]),
        *createMotors(names)
    )

    override var currentPosition: Double
        get() = leader.currentPosition
        set(value) { }

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
            if (names.isEmpty()) {
                throw NotEnoughMotorsException()
            }

            for (i in 1..names.size) {
                list += MotorEx(names[i])
            }

            return list.toTypedArray()
        }
    }
}

class NotEnoughMotorsException(): Exception("You must supply at least one motor to a MotorGroup.")