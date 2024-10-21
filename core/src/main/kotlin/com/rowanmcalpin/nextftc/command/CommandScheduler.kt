package com.rowanmcalpin.nextftc.command

import com.qualcomm.robotcore.util.RobotLog
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.controls.GamepadEx
import com.rowanmcalpin.nextftc.command.groups.CommandGroup
import com.rowanmcalpin.nextftc.command.utility.CustomCommand
import com.rowanmcalpin.nextftc.subsystems.Subsystem
import com.rowanmcalpin.nextftc.command.utility.TelemetryCommand

/**
 * This class runs commands, updates subsystems, and manages Gamepad-related commands. The important
 * functions to use are scheduleCommand(), registerGamepads(), registerSubsystems(), and run().
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object CommandScheduler {

    /**
     * Actively running commands.
     */
    private val runningCommands = mutableListOf<Command>()

    /**
     * Commands that haven't been started yet.
     */
    private val commandsToSchedule = mutableListOf<Command>()
    private val commandsToCancel = mutableMapOf<Command, Boolean>()
    private val gamepads = mutableListOf<GamepadEx>()
    private val subsystems = mutableListOf<Subsystem>()

    /**
     * This function should be run repeatedly every loop. It adds commands if the corresponding
     * Gamepad buttons are being pushed, it runs the periodic functions in Subsystems, it schedules
     * & cancels any commands that need to be started or stopped, and it executes running
     * commands. The reason why it uses a separate function to cancel commands instead of cancelling
     * them itself is because removing items from a list while iterating through that list is a
     * wacky idea.
     */
    // exercise is healthy (and fun!)
    fun run() {
        if (!Constants.opMode.isStopRequested) {
            updateGamepads()
            updateSubsystems()
            scheduleCommands()
            cancelCommands()
            for (command in runningCommands) {
                try {
                    command.execute()
                } catch (e: Exception) {
                    scheduleCommand(
                        TelemetryCommand(30.0, "Error updating " +
                            command.javaClass, e.message ?: "Message Unknown")
                    )
                }


                if (command.isDone) {
                    commandsToCancel += Pair(command, false)
                }
            }
        }
        else {
            cancelAll()
        }
    }

    /**
     * Schedules a command. When multiple commands are scheduled, each of them run in parallel.
     * @param command the command to be scheduled
     */
    fun scheduleCommand(command: Command) {
        commandsToSchedule += command
    }

    /**
     * Registers one or more subsystems. This list is used to run the subsystems' periodic
     * functions.
     * @param subsystems the subsystems to be registered
     */
    fun registerSubsystems(vararg subsystems: Subsystem) {
        for (subsystem in subsystems) {
            subsystem.initialize()
            CommandScheduler.subsystems += subsystem
        }
    }

    /**
     * Registers one or more CustomGamepads. These gamepads will be scanned every loop to see if
     * any buttons are being pressed, and if so, their corresponding commands will be scheduled.
     * @param gamepads the gamepads to be registered
     */
    fun registerGamepads(vararg gamepads: GamepadEx) {
        for (gamepad in gamepads)
            CommandScheduler.gamepads += gamepad
    }

    /**
     * Removes every subsystem and gamepad. This function should generally only be used when an
     * OpMode ends.
     */
    fun unregisterAll() {
        subsystems.clear()
        gamepads.clear()
    }

    /**
     * Cancels every command. This function should generally only be used when an OpMode ends.
     */
    fun cancelAll() {
        for (command in runningCommands) {
            commandsToCancel += Pair(command, true)
        }
        runningCommands.clear()
        cancelCommands()
        commandsToSchedule.clear()
    }

    /**
     * Returns whether or not there are commands running
     */
    fun hasCommands(): Boolean = runningCommands.isNotEmpty()

    /**
     * Initializes every command in the commandsToSchedule list.
     */
    private fun scheduleCommands() {
        for(command in commandsToSchedule) {
            initCommand(command)
        }
        commandsToSchedule.clear()
    }

    /**
     * Cancels every command in the commandsToCancel list.
     */
    private fun cancelCommands() {
        for(pair in commandsToCancel) {
            cancel(pair.key, pair.value)
        }
        commandsToCancel.clear()
    }

    /**
     * Initializes a command. This function first scans to find any conflicts (other commands using
     * the same subsystem). It then checks to see if any of those commands are not interruptible. If
     * some of them aren't interruptible, it ends the initialization process and does not schedule
     * the new command. Otherwise, it cancels the conflicts, runs the new command's start function,
     * and adds it to the list of runningCommands.
     * @param command the new command being initialized
     */
    private fun initCommand(command: Command) {
        for (requirement in command.requirements) {
            val conflicts = findCommands({ it.requirements.contains(requirement) }).toMutableList()
            if (conflicts.contains(command)) {
                conflicts -= command
            }
            for (conflict in conflicts)
                if (!conflict.interruptible) {
                    return
                }
            for (conflict in conflicts)
                commandsToCancel += Pair(command, true)
        }
        try {
            command.start()
            runningCommands += command
        } catch (e: UninitializedPropertyAccessException) {
            val name: String = e.message!!.substring(18, e.message!!.length - 25)
            scheduleCommand(
                TelemetryCommand(30.0, "Error",
                "A lateinit property named $name in the was not initialized. Did you forget" +
                        " to initialize the subsystem running the " + command.javaClass + " command?")
            )
        }
    }

    /**
     * Ends a command and removes it from the runningCommands list.
     * @param command the command being cancelled
     * @param interrupted whether or not that command was interrupted, such as the OpMode is stopped
     *                    prematurely
     */
    private fun cancel(command: Command, interrupted: Boolean = false) {
        try {
            command.end(interrupted)
        } catch (e: RuntimeException) {
            scheduleCommand(
                /*
                val data = "Power: " + Range.clip(power, -min(speed, 1.0), min(speed, 1.0)) + ", direction: " + direction + ", error: " + error
            RobotLog.i("MotorToPosition %s", data)
                 */
                CustomCommand(getDone = {true}, _start = {
                    val data = "Error canceling " + command.javaClass + "Message: " + (e.message ?: "Message unknown")
                    RobotLog.e("CommandScheduler %s", data)
                })
            )
        }
        runningCommands -= command
    }

    /**
     * Runs the update function for each gamepad.
     */
    private fun updateGamepads() {
        for (gamepad in gamepads)
            gamepad.update()
    }

    /**
     * Runs the periodic function for each subsystem and the inUsePeriodic function for each
     * subsystem being used by at least one command.
     */
    private fun updateSubsystems() {
        for (subsystem in subsystems) {
            try {
                subsystem.periodic()
            } catch (e: Exception) {
                scheduleCommand(
                    TelemetryCommand(30.0, "Error running periodic for " +
                        subsystem.javaClass, e.message ?: "Message Unknown")
                )
            }
            if (findCommand({ it.requirements.contains(subsystem) }) != null) {
                try {
                    subsystem.inUsePeriodic()
                } catch (e: Exception) {
                    scheduleCommand(
                        TelemetryCommand(30.0, "Error running" +
                            "inUsePeriodic for " + subsystem.javaClass, e.message ?: "Message Unknown")
                    )
                }
            }
        }
    }

    /**
     * Calls the findCommands() function and uses the first result, or null if there are none
     * @param check the lambda used to determine what kind of command should be found
     * @param commands the list of commands to scan, uses runningCommands by default
     */
    private fun findCommand(check: (Command) -> Boolean, commands : List<Command> = runningCommands) =
        findCommands(check, commands).firstOrNull()

    /**
     * Returns a list of every command in the given list that passes a check. Also scans
     * CommandGroups by recursively calling itself.
     * @param check the lambda used to determine what kind of commands should be found
     * @param commands the list of commands to scan, uses runningCommands by default
     */
    private fun findCommands(check: (Command) -> Boolean, commands : List<Command> = runningCommands):
            List<Command> {
        val foundCommands = mutableListOf<Command>()
        for (command in commands) {
            if (check.invoke(command))
                foundCommands.add(command)
            if (command is CommandGroup) {
                val c = findCommand(check, command.commands)
                if (c != null) foundCommands.add(c)
            }
        }
        return foundCommands
    }
}