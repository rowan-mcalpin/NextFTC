package com.rowanmcalpin.nextftc.ftc.gamepad

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand

/**
 * A joystick has 2 [JoystickAxis] plus a button. 
 * @param xAxisValue the value of the x-axis to watch
 * @param yAxisValue the value of the y-axis to watch
 * @param buttonValue the value of the button to watch
 * @param horizontalThreshold the amount the horizontal axis has to be moved in either direction 
 *                                  before it is considered 'displaced'
 * @param verticalThreshold the amount the vertical axis has to be moved in either direction before
 *                                  it is considered 'displaced'
 *  @param reverseVertical by default, the y-axis of Joysticks are reversed (so pushing away from
 *                                  you decreases the value instead of increasing. When this is true,
 *                                  it will automatically correct for that.
 */
class Joystick(private val xAxisValue: () -> Float, private val yAxisValue: () -> Float, 
               private val buttonValue: () -> Boolean,
               private val horizontalThreshold: Float = 0f, private val verticalThreshold: Float = 0f,
               private val reverseVertical: Boolean = true,): Control() {
    /**
     * This command will be scheduled every time the joystick moves off center. Note that it 
     * receives a pair of floats; these are the x and y values.  
     */
    var displacedCommand: (Pair<Float, Float>) -> Command = { NullCommand() }

    /**
     * This command will be scheduled every time the joystick returns to center. Note that it
     * receives a pair of floats; these are the x and y values.
     */
    var releasedCommand: (Pair<Float, Float>) -> Command = { NullCommand() }

    /**
     * This command will be scheduled every update that the joystick isn't centered. Note that it
     * receives a pair of floats; these are the x and y values.
     */
    var heldCommand: (Pair<Float, Float>) -> Command = { NullCommand() }

    /**
     * This command will be scheduled whenever the state changes. Note that it
     * receives a pair of floats; these are the x and y values.
     */
    var stateChangeCommand: (Pair<Float, Float>) -> Command = { NullCommand() }

    /**
     * The [JoystickAxis] representing horizontal motion.
     */
    var xAxis: JoystickAxis = JoystickAxis(xAxisValue, horizontalThreshold, false)

    /**
     * The [JoystickAxis] representing vertical motion.
     */
    var yAxis: JoystickAxis = JoystickAxis(yAxisValue, verticalThreshold, reverseVertical)

    /**
     * The [Button] of the joystick.
     */
    var button: Button = Button(buttonValue)

    /**
     * X-value of the joystick.
     */
    val x: Float
        get() = xAxis.value

    /**
     * Y-value of the joystick. If [reverseVertical] is true, this will be the corrected direction.
     */
    val y: Float
        get() = yAxis.value


    override fun update() {
        xAxis.update()
        yAxis.update()
        button.update()

        if (xAxis.state || yAxis.state) {
            CommandManager.scheduleCommand(heldCommand(Pair(x, y)))
        }

        if (xAxis.risingState || yAxis.risingState) {
            CommandManager.scheduleCommand(displacedCommand(Pair(x, y)))
        }

        if (xAxis.fallingState || yAxis.fallingState) {
            CommandManager.scheduleCommand(releasedCommand(Pair(x, y)))
        }

        if (xAxis.stateChanged || yAxis.stateChanged) {
            CommandManager.scheduleCommand(stateChangeCommand(Pair(x, y)))
        }
    }
}