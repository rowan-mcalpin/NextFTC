package com.rowanmcalpin.nextftc.ftc.gamepad

/**
 * This is the abstract class that all Controls extend. All controls have a state, and some
 * way to detect whether their state just became true or just became false.
 */
abstract class Control {
    /**
     * The current boolean state of this control.
     */
    var state: Boolean = false

    /**
     * Whether the current boolean state just changed to `true`.
     */
    var risingState: Boolean = false

    /**
     * Whether the current boolean state just changed to `false`.
     */
    var fallingState: Boolean = false

    /**
     * Whether the current boolean state just changed.
     */
    var stateChanged: Boolean = false

    /**
     * Updates the current state and the rising, falling, and changed states. Further, schedules
     * commands accordingly.
     */
    abstract fun update()

    /**
     * Updates whether the control state and whether it is rising or falling.
     */
    protected fun updateState(newState: Boolean) {
        risingState = newState && !state
        fallingState = !newState && state
        stateChanged = newState != state
        state = newState
    } 
}