package com.rowanmcalpin.nextftc.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.TelemetryController
import com.rowanmcalpin.nextftc.command.CommandScheduler
import com.rowanmcalpin.nextftc.controls.Controls
import com.rowanmcalpin.nextftc.controls.DefaultControls
import com.rowanmcalpin.nextftc.driving.drivers.Driver
import com.rowanmcalpin.nextftc.driving.drivers.NullDrive
import com.rowanmcalpin.nextftc.subsystems.Subsystem
import com.rowanmcalpin.nextftc.trajectories.TrajectoryFactory

// Likely want to workshop name
open class NextFTCOpMode(vararg val subsystems: Subsystem = arrayOf()): LinearOpMode() {

    open val controls: Controls = DefaultControls()
    open val color: Constants.Color = Constants.Color.UNKNOWN;
    open val drive: Driver = NullDrive();
    open val trajectoryFactory: TrajectoryFactory? = null;


    fun initializeSubsystems() {
        subsystems.forEach {
            it.initialize()
        }
    }

    fun updateSubsystems() {
        subsystems.forEach {
            it.periodic()
        }
    }

    override fun runOpMode() {
        Constants.opMode = this
        Constants.color = color
        Constants.drive = drive;
        if (trajectoryFactory != null && !trajectoryFactory!!.initialized) {
            trajectoryFactory!!.initialize()
        }
        controls.registerGamepads()

        // Register subsystems
        CommandScheduler.registerSubsystems(TelemetryController, *subsystems)
        CommandScheduler.registerSubsystems(drive)

        onInit()

        // Wait for start
        while (!isStarted && !isStopRequested) {
            CommandScheduler.run()
            onWaitForStart()
        }

        // We don't want to call onStartButtonPressed if we requested stop instead of pressing play
        if (!isStopRequested) {
            onStartButtonPressed()
        }

        // Now we register commands from the controls file
        controls.registerCommands()

        while (!isStopRequested && isStarted) {
            CommandScheduler.run()
            onUpdate()
        }

        onStop()
        CommandScheduler.cancelAll()
        CommandScheduler.unregisterAll()
    }

    open fun onInit() { }

    open fun onWaitForStart() { }

    open fun onStartButtonPressed() { }

    open fun onUpdate() { }

    open fun onStop() { }
}