package com.rowanmcalpin.nextftc.core.command.groups

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * A [CommandGroup] that runs all of its children simultaneously until one of its children is done,
 * at which point it stops all of its children.
 */
class ParallelRaceGroup(vararg commands: Command): CommandGroup(*commands) {
    /**
     * Private variable to be able to manually set interruptible during creation.
     */
    private var _interruptible = true

    override val interruptible
        get() = _interruptible

    /**
     * This will return false until one of its children is done
     */
    override val isDone: Boolean
        get() = children.any { it.isDone }

    /**
     * Overrides the [Command.subsystems] variable to inherit all subsystems from all of its children.
     */
    override val subsystems: Set<Subsystem>
        get() = children.flatMap { it.subsystems }.toSet()


    /**
     * In a Parallel Group, we can just straight away add all of the commands to the CommandManager,
     * which can take care of the rest.
     */
    override fun start() {
        super.start()
        children.forEach {
            CommandManager.scheduleCommand(it)
        }
    }

    /**
     * Sets whether this command is [interruptible]. This functionality is similar to a builder class,
     * so you can use it inline with the Command Group declaration.
     * @param interruptible whether this group should be interruptible
     * @return this Command Group, with [interruptible] set to the value
     */
    fun setInterruptible(interruptible: Boolean): ParallelRaceGroup {
        _interruptible = interruptible
        return this
    }

    override fun stop(interrupted: Boolean) {
        children.forEach {
            CommandManager.cancelCommand(it)
        }
    }
}