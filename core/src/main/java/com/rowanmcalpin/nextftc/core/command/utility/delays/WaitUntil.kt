package com.rowanmcalpin.nextftc.core.command.utility.delays

import com.rowanmcalpin.nextftc.core.command.Command

/**
 * A type of delay that waits until a specified check returns true. Like all delays, if placed 
 * directly in a sequential group, it will accomplish nothing except slowing loop times and taking
 * up memory. 
 * @param check the check to repeatedly check to see if it should continue
 */
class WaitUntil(private val check: () -> Boolean): Command() {
    override val isDone: Boolean
        get() = check()
}