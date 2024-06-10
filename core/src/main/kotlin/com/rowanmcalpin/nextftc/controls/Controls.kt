package com.rowanmcalpin.nextftc.controls

import com.rowanmcalpin.nextftc.command.CommandScheduler
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.hardware.GamepadEx

/**
 * This class manages the controls for TeleOp OpModes. In your project, you should create your own
 * Controls class that inherits from this one and overrides the registerCommands function. If you
 * want to register a command, type a line into registerCommands with the following format:
 * gamepad.button.command = Subsystem.command
 * For example, that could look like this:
 * gamepad1.a.startCommand = Lift.toHigh
 * If you used the line above, then whenever you pressed the "a" button on gamepad1 it would move
 * the lift to the high position.
 */
abstract class Controls {

    protected lateinit var gamepad1: GamepadEx
    protected lateinit var gamepad2: GamepadEx

    /**
     * Registers gamepad1 and gamepad2 with the CommandScheduler so that the CommandScheduler can
     * update them every loop
     */
    fun registerGamepads() {
        gamepad1 = GamepadEx(Constants.opMode.gamepad1)
        gamepad2 = GamepadEx(Constants.opMode.gamepad2)
        CommandScheduler.registerGamepads(gamepad1, gamepad2)
    }

    /**
     * Registers commands on the gamepads.
     * Directions for registering commands are in the class docs.
     */
    abstract fun registerCommands()
}