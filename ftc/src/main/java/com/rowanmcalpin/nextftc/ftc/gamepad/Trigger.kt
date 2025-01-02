package com.rowanmcalpin.nextftc.ftc.gamepad

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand

/**
 * A trigger control that has a value between 0 and 1.
 * @param trigger the value of the trigger to watch
 * @param threshold the amount the trigger has to be pressed before it is considered 'pressed'
 */
class Trigger(private val trigger: () -> Float, private val threshold: Float = 0f): Control() {
    /**
     * This command will be scheduled every time the trigger is pressed.
     */
    var pressedCommand: (Float) -> Command = { NullCommand() }

    /**
     * This command will be scheduled every time the trigger is released.
     */
    var releasedCommand: (Float) -> Command = { NullCommand() }

    /**
     * This command will be scheduled every update that the trigger is held down.
     */
    var heldCommand: (Float) -> Command = { NullCommand() }
    
    /**
     * This command will be scheduled whenever the state changes.
     */
    var stateChangeCommand: (Float) -> Command = { NullCommand() }

    /**
     * The amount the trigger is being pressed.
     */
    var value: Float = 0f

    /**
     * Current profile curve (defaults to linear)
     */
    var profileCurve: (Float) -> Float = { it }

    override fun update() {
        value = trigger()
        updateState(value > threshold)
        
        if (state) {
            CommandManager.scheduleCommand(heldCommand(value))
        }
        
        if (risingState) {
            CommandManager.scheduleCommand(pressedCommand(value))
        }
        
        if (fallingState) {
            CommandManager.scheduleCommand(releasedCommand(value))
        }
        
        if (stateChanged) {
            CommandManager.scheduleCommand(stateChangeCommand(value))
        }
    }
}