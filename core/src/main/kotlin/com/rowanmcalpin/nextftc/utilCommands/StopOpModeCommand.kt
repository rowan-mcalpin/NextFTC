package com.rowanmcalpin.nextftc.utilCommands

import com.rowanmcalpin.nextftc.Command
import com.rowanmcalpin.nextftc.Constants.opMode

class StopOpModeCommand: com.rowanmcalpin.nextftc.Command() {

    override val _isDone: Boolean
        get() = true

    override fun start() {
        opMode.requestOpModeStop()
    }
}