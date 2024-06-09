package org.atomicrobotics3805.cflib.utilCommands

import org.atomicrobotics3805.cflib.Command
import org.atomicrobotics3805.cflib.Constants.opMode

class StopOpModeCommand: Command() {

    override val _isDone: Boolean
        get() = true

    override fun start() {
        opMode.requestOpModeStop()
    }
}