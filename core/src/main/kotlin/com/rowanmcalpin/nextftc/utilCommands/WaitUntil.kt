package com.rowanmcalpin.nextftc.utilCommands

import com.rowanmcalpin.nextftc.Command

/**
 * This class is a type of delay that doesn't finish until a given check returns true. Like other
 * delays, it should be put in a sequential command group before the command that needs to be
 * delayed.
 */
class WaitUntil(private val check: () -> Boolean) : com.rowanmcalpin.nextftc.Command() {

    override val _isDone: Boolean
        get() = check.invoke()
}