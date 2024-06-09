package com.rowanmcalpin.nextftc.utilCommands

import com.qualcomm.robotcore.util.ElapsedTime
import com.rowanmcalpin.nextftc.Command

/**
 * This class is a simple delay that waits for a certain amount of time. Like other delays, it
 * should be put in a sequential command group before the command that needs to be delayed. A delay in a parallel 
 * command group will accomplish nothing except taking up memory on the device.
 */
@SuppressWarnings("unused")
class Delay(private val time: Double): com.rowanmcalpin.nextftc.Command() {
    override val _isDone: Boolean
        get() = timer.seconds() > time

    private val timer = ElapsedTime()

    /**
     * Resets timer
     */
    override fun start() {
        timer.reset()
    }
}