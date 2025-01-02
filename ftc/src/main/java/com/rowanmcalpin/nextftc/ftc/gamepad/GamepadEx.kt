/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rowanmcalpin.nextftc.ftc.gamepad

import com.qualcomm.robotcore.hardware.Gamepad

/**
 * This class offers a complete wrapper of the Qualcomm gamepad class. The only time users should
 * need to interact with the Qualcomm gamepad class is when instantiating this class.
 * 
 * @param gamepad the Qualcomm gamepad to wrap
 */
class GamepadEx(private val gamepad: Gamepad, triggerThreshold: Float = 0f, 
                horizontalJoystickThreshold: Float = 0f, verticalJoystickThreshold: Float = 0f, 
                reverseVertical: Boolean = true) {
    val a = Button { gamepad.a }
    val b = Button { gamepad.b }
    val x = Button { gamepad.x }
    val y = Button { gamepad.y }
    
    val dpadUp = Button { gamepad.dpad_up }
    val dpadDown = Button { gamepad.dpad_down }
    val dpadLeft = Button { gamepad.dpad_left }
    val dpadRight = Button { gamepad.dpad_right }
    
    val leftBumper = Button { gamepad.left_bumper }
    val rightBumper = Button { gamepad.right_bumper }
    
    val leftTrigger = Trigger({ gamepad.left_trigger }, triggerThreshold)
    val rightTrigger = Trigger({ gamepad.right_trigger }, triggerThreshold)
    
    val leftStick = Joystick({ gamepad.left_stick_x }, { gamepad.left_stick_y }, 
        { gamepad.left_stick_button }, horizontalJoystickThreshold, verticalJoystickThreshold, reverseVertical)
    val rightStick = Joystick({ gamepad.right_stick_x }, { gamepad.right_stick_y },
        { gamepad.right_stick_button }, horizontalJoystickThreshold, verticalJoystickThreshold, reverseVertical)

    /**
     * A list of all of the controls this gamepad has.
     */
    val controls = listOf(a, b, x, y, dpadUp, dpadDown, dpadLeft, dpadRight, leftBumper, rightBumper,
        leftTrigger, rightTrigger, leftStick, rightStick)
    
    fun update() {
        controls.forEach {
            it.update()
        }
    }
}