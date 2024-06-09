/*
    Copyright (c) 2022 Atomic Robotics (https://atomicrobotics3805.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.rowanmcalpin.nextftc.roadrunner

import java.lang.IllegalStateException

/**
 * IMU axes signs in the order XYZ (after remapping).
 */
enum class AxesSigns(val bVal: Int) {
    PPP(0), PPN(1), PNP(2), PNN(3), NPP(4), NPN(5), NNP(6), NNN(7);

    companion object {
        fun fromBinaryValue(bVal: Int): AxesSigns {
            val maskedVal = bVal and 0x07
            return when (maskedVal) {
                0 -> PPP
                1 -> PPN
                2 -> PNP
                3 -> PNN
                4 -> NPP
                5 -> NPN
                6 -> NNP
                7 -> NNN
                else -> throw IllegalStateException("Unexpected value for maskedVal: $maskedVal")
            }
        }
    }
}