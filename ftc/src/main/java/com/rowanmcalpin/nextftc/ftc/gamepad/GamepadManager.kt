package com.rowanmcalpin.nextftc.ftc.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import com.rowanmcalpin.nextftc.core.command.Command

class GamepadManager(gamepad1: Gamepad, gamepad2: Gamepad) {
    val gamepad1: GamepadEx = GamepadEx(gamepad1)
    val gamepad2: GamepadEx = GamepadEx(gamepad2)
    
    fun updateGamepads() {
        gamepad1.update()
        gamepad2.update()
    }
    
    inner class GamepadUpdaterCommand: Command() {
        override val isDone: Boolean = false

        override fun update() {
            updateGamepads()
        }
    }
}