package com.rowanmcalpin.nextftc.core

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand

/**
 * A [Subsystem] represents a real-world system (such as a lift, intake, or claw) that cannot be
 * controlled by multiple commands simultaneously.
 */
abstract class Subsystem {
    open val defaultCommand: () -> Command = { NullCommand() }

    /**
     * Initializes this subsystem. This function is perfect for calling `hardwareMap.get` or
     * otherwise initializing hardware devices. 
     */
    open fun initialize() { }

    /**
     * This function is called every update.
     */
    open fun periodic() { }
}