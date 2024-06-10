package com.rowanmcalpin.nextftc.command.utility

import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.Constants.opMode

class StopOpModeCommand: Command() {

    override val _isDone: Boolean
        get() = true

    override fun start() {
        opMode.requestOpModeStop()
    }
}