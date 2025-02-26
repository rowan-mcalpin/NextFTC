package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

class PerpetualCommand(val command: Command): Command() {

    override val subsystems: Set<Subsystem> = command.subsystems

    override val isDone: Boolean = false

    override fun start() = command.start()

    override fun update() = command.update()

    override fun stop(interrupted: Boolean) = command.stop(interrupted)
}