/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 *     Copyright (C) 2025 Rowan McAlpin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.rowanmcalpin.nextftc.ftc

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.SubsystemGroup
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager


/**
 * This is a wrapper class for an OpMode that does the following:
 *  - Automatically initializes and runs the CommandManager
 *  - If desired, automatically implements and handles Gamepads
 */
open class NextFTCOpMode(vararg var subsystems: Subsystem = arrayOf()): LinearOpMode() {

    open lateinit var gamepadManager: GamepadManager

    /**
     * Whether to bulk read the hubs. It is recommended to leave this ON. You must only update this
     * in [onInit]. If you update it in [onUpdate] or from a command, you will likely break things.
     */
    var useBulkReading = true

    private lateinit var allHubs: List<LynxModule>

    override fun runOpMode() {
        OpModeData.opMode = this
        OpModeData.hardwareMap = hardwareMap
        OpModeData.gamepad1 = gamepad1
        OpModeData.gamepad2 = gamepad2

        gamepadManager = GamepadManager(gamepad1, gamepad2)

        CommandManager.runningCommands.clear()
        expandSubsystems()
        initSubsystems()
        onInit()

        if (useBulkReading) {
            allHubs = hardwareMap.getAll(LynxModule::class.java)

            allHubs.forEach {
                it.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
            }
        }

        // We want to continually update the gamepads
        CommandManager.scheduleCommand(gamepadManager.GamepadUpdaterCommand())

        // Wait for start
        while (!isStarted && !isStopRequested) {
            subsystems.forEach {
                it.periodic()

                // Check if there are any commands running that use the subsystem, or if we can safely
                // schedule its default command
                if (!CommandManager.hasCommandsUsing(it)) {
                    CommandManager.scheduleCommand(it.defaultCommand)
                }
            }
            CommandManager.run()
            onWaitForStart()

            if (useBulkReading) {
                allHubs.forEach {
                    it.clearBulkCache()
                }
            }
        }

        // If we pressed stop after init (instead of start) we want to skip the rest of the OpMode
        // and jump straight to the end
        if (!isStopRequested) {
            onStartButtonPressed()

            while (!isStopRequested && isStarted) {
                subsystems.forEach {
                    it.periodic()

                    // Check if there are any commands running that use the subsystem, or if we can safely
                    // schedule its default command
                    if (!CommandManager.hasCommandsUsing(it)) {
                        CommandManager.scheduleCommand(it.defaultCommand)
                    }
                }
                CommandManager.run()
                onUpdate()

                if (useBulkReading) {
                    allHubs.forEach {
                        it.clearBulkCache()
                    }
                }
            }
        }

        onStop()
        // Since users might schedule a command that stops things, we want to be able to run it 
        // (one update of it, anyways) before we cancel all of our commands.
        CommandManager.run()
        CommandManager.cancelAll()
    }

    /**
     * Called internally to initialize subsystems.
     */
    private fun initSubsystems() {
        subsystems.forEach {
            it.initialize()
        }
    }

    /**
     * Expands SubsystemGroups into a single-layer array (and puts that back into the [subsystems]
     * array)
     */
    private fun expandSubsystems() {
        val expanded = mutableListOf<Subsystem>()

        for (subsystem in subsystems) {
            if (subsystem is SubsystemGroup) {
                expanded += expandSubsystemGroup(subsystem)
            }
            expanded += subsystem
        }

        subsystems = expanded.toTypedArray()
    }

    /**
     * Expands a subsystem group (recursively)
     */
    private fun expandSubsystemGroup(group: SubsystemGroup): Array<Subsystem> {
        val expanded = mutableListOf<Subsystem>()

        for (child in group.subsystems) {
            if (child is SubsystemGroup) {
                expanded += expandSubsystemGroup(child)
            }
            expanded += child
        }

        return expanded.toTypedArray()
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