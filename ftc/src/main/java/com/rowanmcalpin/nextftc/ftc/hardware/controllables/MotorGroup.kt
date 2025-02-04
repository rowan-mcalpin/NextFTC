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

package com.rowanmcalpin.nextftc.ftc.hardware.controllables

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