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

package com.rowanmcalpin.nextftc.ftc

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.ftc.components.NextComponent


/**
 * This is a wrapper class for an OpMode that does the following:
 *  - Automatically initializes and runs the CommandManager
 *  - If desired, automatically implements and handles Gamepads
 */
open class NextFTCOpMode(vararg var components: NextComponent): LinearOpMode() {
    override fun runOpMode() {
        processAnnotations() // Identify whether this is an Autonomous or a TeleOp

        OpModeData.opMode = this
        OpModeData.hardwareMap = hardwareMap
        OpModeData.gamepad1 = gamepad1
        OpModeData.gamepad2 = gamepad2
        OpModeData.telemetry = telemetry

        CommandManager.runningCommands.clear()

        components.forEach {
            it.preInit()
        }
        onInit()
        components.reversed().forEach {
            it.postInit()
        }

        // Wait for start
        while (!isStarted && !isStopRequested) {
            components.forEach {
                it.preWaitForStart()
            }
            CommandManager.run()
            onWaitForStart()
            components.reversed().forEach {
                it.postWaitForStart()
            }
        }

        // If we pressed stop after init (instead of start) we want to skip the rest of the OpMode
        // and jump straight to the end
        if (!isStopRequested) {
            components.forEach {
                it.preStartButtonPressed()
            }
            onStartButtonPressed()
            components.reversed().forEach {
                it.postStartButtonPressed()
            }

            while (!isStopRequested && isStarted) {
                components.forEach {
                    it.preUpdate()
                }
                CommandManager.run()
                onUpdate()
                components.forEach {
                    it.postUpdate()
                }
            }
        }

        components.forEach {
            it.preStop()
        }
        onStop()
        // Since users might schedule a command that stops things, we want to be able to run it
        // (one update of it, anyways) before we cancel all of our commands.
        CommandManager.run()
        CommandManager.cancelAll()
        components.reversed().forEach {
            it.postStop()
        }
    }

    /**
     * This class automatically identifies what type of OpMode it is annotated as, thereby allowing
     * it to set the [OpModeData.opModeType] variable correctly.
     */
    private fun processAnnotations() {
        for (annotation in this::class.annotations) {
            if (annotation is TeleOp) {
                OpModeData.opModeType = OpModeData.OpModeType.TELEOP
            }
            if (annotation is Autonomous) {
                OpModeData.opModeType = OpModeData.OpModeType.AUTO
            }
        }
    }

    /**
     * This function runs ONCE when the init button is pressed.
     */
    open fun onInit() { }

    /**
     * This function runs REPEATEDLY during initialization.
     */
    open fun onWaitForStart() { }

    /**
     * This function runs ONCE when the start button is pressed.
     */
    open fun onStartButtonPressed() { }

    /**
     * This function runs REPEATEDLY when the OpMode is running.
     */
    open fun onUpdate() { }

    /**
     * This function runs ONCE when the stop button is pressed.
     */
    open fun onStop() { }
}