package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * A [Command] that is created using lambdas to define each function instead of manually overriding
 * the functions.
 * @param isDoneLambda a lambda that returns whether the command has finished running
 * @param startLambda a lambda to be called once when the command is first added
 * @param updateLambda a lambda to be called repeatedly while the command is running
 * @param stopLambda a lambda to be called once when the command has finished
 * @param subsystemCollection a set of subsystems this command implements
 * @param interruptible whether this command can be stopped due to an overlap of subsystems
 */
open class LambdaCommand @JvmOverloads constructor(
    private val isDoneLambda: () -> Boolean = { true },
    private val startLambda: () -> Unit = { },
    private val updateLambda: () -> Unit = { },
    private val stopLambda: (interrupted: Boolean) -> Unit = { },
    private val subsystemCollection: Set<Subsystem> = setOf(),
    override var interruptible: Boolean = true
): Command() {
    
    override val subsystems: Set<Subsystem>
        get() = subsystemCollection
    
    override val isDone: Boolean
        get() = isDoneLambda()

    override fun start() {
        startLambda()
    }

    override fun update() {
        updateLambda()
    }

    override fun stop(interrupted: Boolean) {
        stopLambda(interrupted)
    }
    
    
}