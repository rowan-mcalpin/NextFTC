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
package com.rowanmcalpin.nextftc

import com.qualcomm.robotcore.hardware.Gamepad

/**
 * By default, the Qualcomm Gamepad class has very little functionality. It tells you whether each
 * button is being pressed, but not whether it was just pressed or just released. This class adds
 * that for each button, and also allows you to set commands that will be automatically scheduled
 * whenever a button is pressed or released. You should create an instance of this class for both
 * gamepads and then register them in the CommandScheduler using the registerGamepads() function.
 *
 * @param gamepad the Qualcomm gamepad used. Should be gamepad1 or gamepad2
 */
@Suppress("MemberVisibilityCanBePrivate")
class GamepadEx(private val gamepad: Gamepad) {
    val a = Button("A")
    val b = Button("B")
    val x = Button("X")
    val y = Button("Y")

    val dpadUp = Button("D-Pad Up")
    val dpadDown = Button("D-Pad Down")
    val dpadLeft = Button("D-Pad Left")
    val dpadRight = Button("D-Pad Right")

    val leftBumper = Button("Left Bumper")
    val rightBumper = Button("Right Bumper")

    val leftTrigger = Trigger("Left Trigger")
    val rightTrigger = Trigger("Right Trigger")

    val leftStick = JoyStick("Left Stick")
    val rightStick = JoyStick("Right Stick")

    val controls = listOf(a, b, x, y, dpadUp, dpadDown, dpadLeft, dpadRight,
        leftBumper, rightBumper, leftTrigger, rightTrigger, leftStick, rightStick)

    /**
     * Updates each of the buttons with their corresponding values from the Qualcomm gamepad. The
     * reason why we cannot loop through the controls list and update them that way is because each
     * control needs its own value from the Qualcomm gamepad.
     * @param gamepad the Qualcomm gamepad to use,  by default this is the gamepad given as a
     *                constructor parameter
     */
    fun update(gamepad: Gamepad = this.gamepad) {
        a.update(gamepad.a)
        b.update(gamepad.b)
        x.update(gamepad.x)
        y.update(gamepad.y)

        dpadUp.update(gamepad.dpad_up)
        dpadDown.update(gamepad.dpad_down)
        dpadLeft.update(gamepad.dpad_left)
        dpadRight.update(gamepad.dpad_right)

        leftBumper.update(gamepad.left_bumper)
        rightBumper.update(gamepad.right_bumper)

        leftTrigger.update(gamepad.left_trigger)
        rightTrigger.update(gamepad.right_trigger)

        leftStick.update(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.left_stick_button)
        rightStick.update(gamepad.right_stick_x, gamepad.right_stick_y, gamepad.right_stick_button)
    }

    /**
     * Combines the toString() functions of each individual control and places a line between them.
     * Essentially creates a list of which buttons are being pressed.
     * @return the toString() functions of each control
     */
    override fun toString(): String {
        val set = mutableSetOf<String>()

        controls.forEach {
            if(it.toString() != "")
                set.add(it.toString())
        }

        return set.joinToString("\n")
    }

    /**
     * This class represents a button with two possible states (pressed or not pressed). It should
     * not be used for triggers, since those have more than two states.
     * Each button has variables representing whether the button is currently down, whether it was
     * just pressed, and whether it was released. It also has a startCommand and a releasedCommand,
     * which are automatically scheduled when a button is pressed or released (respectively). If
     * @param name this name is used in the toString() function and is set to UnknownButton by
     *             default.
     */
    class Button(private val name: String = "Unknown Button") {

        var down = false
        var pressed = false
        var released = false
        var toggleState = 0
        var pressedCommand: (() -> com.rowanmcalpin.nextftc.Command)? = null
        var releasedCommand: (() -> com.rowanmcalpin.nextftc.Command)? = null
        var toggleCommands: List<() -> com.rowanmcalpin.nextftc.Command>? = null

        /**
         * Updates whether the trigger was just pressed, just released, and whether it's being held
         * down, and then runs the startCommand or releasedCommand if the trigger was just pressed
         * or released.
         * @param value whether the button is being held down right now
         */
        fun update(value: Boolean) {
            // Update whether or not the button is pressed, released, or down
            pressed = value && !down
            released = !value && down
            down = value
            // Run the associated commands
            if (pressed && toggleCommands != null && toggleCommands!!.isNotEmpty()) {
                if (toggleState >= toggleCommands!!.size) {
                    toggleState = 0
                }
                CommandScheduler.scheduleCommand(toggleCommands!![toggleState].invoke())
                toggleState++
            }
            if (pressed && pressedCommand != null) {
                CommandScheduler.scheduleCommand(pressedCommand!!.invoke())
            }
            if (released && releasedCommand != null) {
                CommandScheduler.scheduleCommand(releasedCommand!!.invoke())
            }
        }

        /**
         * Returns a string showing whether the button is down, was just pressed, and/or was just
         * released.
         * @return whether the button is down, was just pressed, and/or was just released.
         */
        override fun toString(): String {
            val set = mutableSetOf<String>()
            if (down) set.add("Down")
            if (pressed) set.add("Just Pressed")
            if (released) set.add("Just Released")
            return if (set.isNotEmpty()) "$name: ${set.joinToString(", ")}" else ""
        }
    }

    /**
     * This class is very similar to the button class. The only difference is that it also has a
     * variable called amount, which is how much the trigger is currently being pressed. The pressed
     * variable is true if the trigger went from not being pressed at all to being pressed at least
     * somewhat, and the released variable is the opposite.
     * @param name this name is used in the toString() function and is set to UnknownTrigger by
     *             default.
     */
    class Trigger(private val name: String = "Unknown Trigger") {
        val down: Boolean
            get() = amount != 0.0f
        var pressed = false
        var released = false
        var amount = 0.0f
        var toggleState = 0
        var pressedCommand: (() -> com.rowanmcalpin.nextftc.Command)? = null
        var releasedCommand: (() -> com.rowanmcalpin.nextftc.Command)? = null
        var toggleCommands: List<() -> com.rowanmcalpin.nextftc.Command>? = null

        /**
         * Updates whether the trigger was just pressed, just released, and how much it's being held
         * down, and then runs the startCommand or releasedCommand if the trigger was just pressed
         * or released.
         * @param value how much the trigger is being pressed down
         */
        fun update(value: Float) {
            pressed = value != 0.0f && !down
            released = value == 0.0f && down
            amount = value
            // Run the associated commands
            if (pressed && toggleCommands != null && toggleCommands!!.isNotEmpty()) {
                if (toggleState > toggleCommands!!.size) {
                    toggleState = 0
                }
                CommandScheduler.scheduleCommand(toggleCommands!![toggleState].invoke())
                toggleState++
            }
            if (pressed && pressedCommand != null) {
                CommandScheduler.scheduleCommand(pressedCommand!!.invoke())
            }
            if (released && releasedCommand != null) {
                CommandScheduler.scheduleCommand(releasedCommand!!.invoke())
            }
        }

        /**
         * Returns a string showing whether the trigger was just pressed, was just released, and/or
         * how much it's being pressed down.
         * @return whether the trigger was just pressed, was just released, and/or how much it's
         * being pressed down.
         */
        override fun toString(): String {
            val set = mutableSetOf<String>()
            if (pressed) set.add("Just Pressed")
            if (released) set.add("Just Released")
            if (amount != 0.0f) set.add("Amount Pressed: $amount")
            return if (set.isNotEmpty()) "$name: ${set.joinToString(", ")}" else ""
        }
    }

    /**
     * This is the most complicated of the controls. It has justMoved and justStopped variables,
     * like the pressed and released variables in the Button class, and also has its own instance of
     * the button class that corresponds to the Joystick button.
     * @param name this name is used in the toString() function and is set to UnknownJoystick by
     *             default.
     */
    class JoyStick(private val name: String = "Unknown Joystick") {
        val moved: Boolean
            get() = x != 0.0f || y != 0.0f
        var justMoved = false
        var justStopped = false
        var x = 0.0f
        var y = 0.0f
        val button = Button()

        /**
         * Updates the x and y values of the joystick, whether it just moved or just stopped, and
         * also runs the update function for the trigger button.
         * @param x the x (left-right) coordinate of the joystick. Left is negative, and right is
         *          positive.
         * @param y the y (up-down) coordinate of the joystick. Up is negative, and down is
         *          positive.
         * @param buttonValue whether the joystick button is being held down or not.
         */
        fun update(x: Float, y: Float, buttonValue: Boolean) {
            justMoved = x != 0.0f || y != 0.0f && !moved
            justStopped = x == 0.0f && y == 0.0f && moved
            this.x = x
            this.y = y
            button.update(buttonValue)
        }

        /**
         * Returns a string showing whether the button was just pressed, was just released, and/or
         * how much it's being pressed down, as well as whether the joystick just started or
         * stopped moving, and its x and y coordinates (if they aren't 0).
         * @return information about the joystick's current state
         */
        override fun toString(): String {
            val set = mutableSetOf<String>()
            if (button.down) set.add("Button Down")
            if (button.pressed) set.add("Button Just Pressed")
            if (button.released) set.add("Button Just Released")
            if (justMoved) set.add("Just Started Moving")
            if (justStopped) set.add("Just Stopped Moving")
            if (set.isNotEmpty() || x != 0.0f || y != 0.0f) {
                set.add("X: $x")
                set.add("Y: $y")
            }
            return if (set.isNotEmpty()) "$name: ${set.joinToString(", ")}" else ""
        }
    }

    /**
     * This command shakes the provided Gamepad using a given rumble effect (both motors at 100% for
     * half a second by default).
     */
    class Shake(private val gamepad: Gamepad, private val rumbleEffect: () -> Gamepad.RumbleEffect =
        {
            Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 500)  //  Rumble both motors 100% for 500 mSec
                .build()
        }
    ): com.rowanmcalpin.nextftc.Command() {
        override val _isDone = true
        override val interruptible = true

        override fun start() {
            gamepad.runRumbleEffect(rumbleEffect.invoke())
        }
    }
}