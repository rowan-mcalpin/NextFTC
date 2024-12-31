package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.Subsystem

/**
 * This is a LambdaCommand that sets isDone to true instantly. As such, there is no update or stop
 * lambda (since the command finishes instantly). All code should be put in the startLambda.
 *
 * @param lambda the lambda to execute
 * @param subsystemCollection a set of subsystems this command implements
 */
class InstantCommand @JvmOverloads constructor(lambda: () -> Unit = { },
           subsystemCollection: Set<Subsystem> = setOf()): LambdaCommand({ true }, lambda,
            {  }, {  }, subsystemCollection, true) {

    @JvmOverloads
    constructor(lambda: () -> Unit = { }, subsystem: Subsystem): this(lambda, setOf(subsystem))
}