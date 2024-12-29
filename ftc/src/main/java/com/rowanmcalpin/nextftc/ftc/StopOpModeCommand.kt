package com.rowanmcalpin.nextftc.ftc

import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This command stops the active OpMode when it is scheduled. 
 */
class StopOpModeCommand: Command() {
    override val isDone = true

    override fun start() {
        OpModeData.opMode.requestOpModeStop()
    }
}
