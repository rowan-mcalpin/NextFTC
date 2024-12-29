package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * A [Command] that is created using lambdas to define each function instead of manually overriding
 * the functions. The difference between this and a normal [LambdaCommand] is that this waits to
 * stop until a certain number of milliseconds have passed.
 * @param isDoneLambda a lambda that returns whether the command has finished running
 * @param startLambda a lambda to be called once when the command is first added
 * @param updateLambda a lambda to be called repeatedly while the command is running
 * @param stopLambda a lambda to be called once when the command has finished
 * @param endTime the end time for this command, in milliseconds after the starting of this command
 * @param subsystemCollection a set of subsystems this command implements
 * @param interruptible whether this command can be stopped due to an overlap of subsystems
 */
class TimedLambdaCommand(
    private val isDoneLambda: () -> Boolean = { true },
    private val startLambda: () -> Unit = { },
    private val updateLambda: () -> Unit = { },
    private val stopLambda: (interrupted: Boolean) -> Unit = { },
    private val endTime: Long = 0L,
    private val subsystemCollection: Set<Subsystem> = setOf(),
    override var interruptible: Boolean = true
): Command() {
    
    private var startTime: Long = 0L
    
    override val subsystems: Set<Subsystem>
        get() = subsystemCollection
    
    override val isDone: Boolean
        get() = (isDoneLambda() && System.currentTimeMillis() - startTime >= endTime)

    override fun start() {
        startTime = System.currentTimeMillis()
        startLambda()
    }

    override fun update() {
        updateLambda()
    }

    override fun stop(interrupted: Boolean) {
        stopLambda(interrupted)
    }
}