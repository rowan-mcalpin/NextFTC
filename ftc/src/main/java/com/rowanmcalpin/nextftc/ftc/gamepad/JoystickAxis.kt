package com.rowanmcalpin.nextftc.ftc.gamepad

import kotlin.math.abs

/**
 * An axis of a joystick that has a value between -1 and 1.
 * @param axis the value of the axis to watch
 * @param threshold the amount the axis has to be moved in either direction before it is considered 
 * 'displaced'
 */
class JoystickAxis(private val axis: () -> Float, private val threshold: Float = 0f, private val reverse: Boolean): Control() {
    /**
     * The amount the joystick is being moved.
     */
    var value: Float = 0f
    
    override fun update() {
        value = axis() * if (reverse) -1 else 1
        updateState(abs(value) > threshold)
    }
}