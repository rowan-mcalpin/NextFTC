package com.rowanmcalpin.nextftc.command.utility

import com.qualcomm.hardware.lynx.LynxModule
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.CommandScheduler
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit

/**
 * A customizable voltage watcher that runs a command when the voltage drops below a certain threshold. The command it
 * runs defaults to the `StopOpModeCommand`.
 */
class VoltageWatcher(
    private val controlHubName: String = "Control Hub",
    private val command: Command = StopOpModeCommand(),
    private val unit: VoltageUnit = VoltageUnit.VOLTS,
    private val threshold: Double = 6.0
): Command() {
    override val _isDone = false

    private lateinit var controlHub: LynxModule

    override fun onStart() {
        controlHub = Constants.opMode.hardwareMap.get(LynxModule::class.java, controlHubName)
    }

    override fun onExecute() {
        if (controlHub.getInputVoltage(unit) < threshold) {
            // The voltage is too low. Stop the OpMode
            CommandScheduler.scheduleCommand(command)
        }
    }
}