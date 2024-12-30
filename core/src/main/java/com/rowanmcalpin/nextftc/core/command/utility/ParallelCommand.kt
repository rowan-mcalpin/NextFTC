package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * This is a command that will run in parallel even if it's in a SequentialGroup.
 *
 * @param command the command to run in parallel
 */
class ParallelCommand(val command: () -> Command): Command() {
    override val isDone = true

    override fun start() {
        CommandManager.scheduleCommand(command())
    }
}