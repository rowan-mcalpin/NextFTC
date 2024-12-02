package com.rowanmcalpin.nextftc.controls

/**
 * Defines an arbitrary curve that takes a raw gamepad input and outputs a value.
 */
abstract class JoyStickCurve {
    abstract fun calculate(input: Float): Float
}

/**
 * The default JoyStickCurve, a 1:1 correspondence input to output.
 */
class LinearJoyStickCurve : JoyStickCurve() {
    override fun calculate(input: Float): Float {
        return input
    }
}