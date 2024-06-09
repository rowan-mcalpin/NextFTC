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