package com.rowanmcalpin.nextftc.command.utility

import com.qualcomm.hardware.lynx.LynxModule
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.command.Command
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit

/**
 * A basic voltage watcher that stops the OpMode if the voltage drops below a certain threshold.
 */
class BasicVoltageWatcher(
    private val controlHubName: String = "Control Hub",
    private val unit: VoltageUnit = VoltageUnit.VOLTS,
    private val threshold: Double = 6.0
): Command() {
    override val _isDone = false

    lateinit var controlHub: LynxModule

    override fun onStart() {
        controlHub = Constants.opMode.hardwareMap.get(LynxModule::class.java, controlHubName)
    }

    override fun onExecute() {
        if (controlHub.getInputVoltage(unit) < threshold) {
            // The voltage is too low. 
        }
    }
}