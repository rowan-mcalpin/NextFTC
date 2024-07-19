package com.rowanmcalpin.nextftc.command.utility

import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.CommandScheduler

class SwitchCommand(
    private val value: () -> Any,
    private vararg val outcomes: Pair<Any, Command>
) : Command() {

    override val _isDone: Boolean
        get() = true

    override fun onStart() {
        val invokedValue: Any = value.invoke()
        outcomes.forEach {
            if (invokedValue == it.first) {
                CommandScheduler.scheduleCommand(it.second)
            }
        }
    }
}