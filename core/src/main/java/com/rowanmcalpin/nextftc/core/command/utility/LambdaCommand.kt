/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 *     Copyright (C) 2025 Rowan McAlpin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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