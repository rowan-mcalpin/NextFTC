package com.rowanmcalpin.nextftc.utilCommands

import com.rowanmcalpin.nextftc.Command
import com.rowanmcalpin.nextftc.Constants.opMode

class StopOpModeCommand: Command() {

    override val _isDone: Boolean
        get() = true

    override fun start() {
        opMode.requestOpModeStop()
    }
}