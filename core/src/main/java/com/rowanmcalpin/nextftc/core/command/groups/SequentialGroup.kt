package com.rowanmcalpin.nextftc.core.command.groups

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * A [CommandGroup] that runs its children one at a time.
 */
class SequentialGroup(vararg commands: Command): CommandGroup(*commands) {
    /**
     * This returns true once all of its children have finished running.
     */
    override val isDone: Boolean
        get() = children.size == 0

    /**
     * In a Sequential Group, we will start the first command and wait until it has completed
     * execution before starting the next.
     */
    override fun start() {
        super.start()
        CommandManager.scheduleCommand(children[0])
    }

    /**
     * Now, every update we must check if the currently active command is complete. If it is, remove
     * it and start the next one (if there is one).
     */
    override fun update() {
        // If the first child is done running, remove it and start the next one.
        if (children[0].isDone) {
            children.removeAt(0)

            // Now, if there is another command to run, start it. 
            if (children.size > 0) {
                CommandManager.scheduleCommand(children[0])
            }
        }
    }
}