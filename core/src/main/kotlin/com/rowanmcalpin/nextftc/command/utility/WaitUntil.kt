package com.rowanmcalpin.nextftc.command.utility

import com.rowanmcalpin.nextftc.command.Command

/**
 * This class is a type of delay that doesn't finish until a given check returns true. Like other
 * delays, it should be put in a sequential command group before the command that needs to be
 * delayed.
 */
class WaitUntil(private val check: () -> Boolean) : Command() {

    override val _isDone: Boolean
        get() = check.invoke()
}