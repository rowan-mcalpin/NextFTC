/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * A [Command] that is created using lambdas to define each function instead of manually overriding
 * the functions.
 * @author BeepBot99
 */

open class LambdaCommand: Command() {
    private var isDoneLambda = Supplier<Boolean> { true }
    private var startLambda = Runnable { }
    private var updateLambda = Runnable { }
    private var stopLambda = Consumer<Boolean> { }
    override var subsystems: Set<Subsystem> = setOf()
    override var interruptible: Boolean = true

    override val isDone: Boolean get() = isDoneLambda.get()
    override fun start() = startLambda.run()
    override fun update() = updateLambda.run()
    override fun stop(interrupted: Boolean) = stopLambda.accept(interrupted)

    /**
     * Sets the function that is called when the command is first scheduled
     */
    fun setStart(start: Runnable) = apply { startLambda = start }

    /**
     * Sets the function that is called repeatedly while the command is running
     */
    fun setUpdate(update: Runnable) = apply { updateLambda = update }

    /**
     * Sets the function that is called when the command is finished
     * Receives a boolean that is whether the command has been interrupted
     */
    fun setStop(stop: Consumer<Boolean>) = apply { stopLambda = stop }

    /**
     * Sets the subsystems that the command implements
     */
    fun setSubsystems(subsystemsSet: Set<Subsystem>) = apply { subsystems = subsystemsSet }

    /**
     * Sets the subsystems that the command implements
     */
    fun setSubsystems(vararg subsystemsArr: Subsystem) = apply { subsystems = subsystemsArr.toSet() }

    /**
     * Sets the subsystem that the command implements
     */
    fun setSubsystem(subsystem: Subsystem) = apply { subsystems = setOf(subsystem) }

    /**
     * Adds subsystems to the set of subsystems that the command implements
     */
    fun addSubsystems(subsystemsSet: Set<Subsystem>) = apply { subsystems += subsystemsSet }

    /**
     * Adds subsystems to the set of subsystems that the command implements
     */
    fun addSubsystems(vararg subsystemsArr: Subsystem) = apply{ subsystems += subsystemsArr.toSet() }

    /**
     * Adds a subsystem to the set of subsystems that the command implements
     */
    fun addSubsystem(subsystem: Subsystem) = apply { subsystems += subsystem }

    /**
     * Sets the function that returns whether the command has finished running
     */
    fun setIsDone(done: Supplier<Boolean>) = apply { isDoneLambda = done }

    /**
     * Sets whether the command can be stopped due to an overlap of subsystems
     */
    fun setInterruptible(isInterruptible: Boolean) = apply { interruptible = isInterruptible }
}