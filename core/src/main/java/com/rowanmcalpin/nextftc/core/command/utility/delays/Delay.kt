package com.rowanmcalpin.nextftc.core.command.utility.delays

import com.rowanmcalpin.nextftc.core.command.Command

/**
 * A [Command] that does nothing except wait until a certain amount of time has passed. Like all 
 * delays, if placed directly in a sequential group, it will accomplish nothing except slowing loop 
 * times and taking up memory.
 * @param time the desired duration of this command, in seconds
 */
class Delay(
    private val time: Double = 0.0
): Command() {
    
    private var startTime: Double = 0.0
    
    override val isDone: Boolean
        get() = (System.nanoTime() / 1E9) - startTime >= time

    override fun start() {
        startTime = System.nanoTime().toDouble() / 1E9
    }
}