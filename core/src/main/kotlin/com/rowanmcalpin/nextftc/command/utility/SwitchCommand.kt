package com.rowanmcalpin.nextftc.command.utility

import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.CommandScheduler
import com.rowanmcalpin.nextftc.subsystems.Subsystem

/**
 * This class represents a command that runs different commands based on the result of a
 * provided value function. The appropriate command(s) are scheduled if their corresponding value
 * is matched. The value is checked at the start of the command, and the selected commands execute
 * repeatedly until they are completed.
 */
class SwitchCommand(
    private val value: () -> Any,
    private vararg val outcomes: Pair<Any, Command>
) : Command() {

    /**
     * This property returns true if every command is finished and has been removed from the list.
     */
    override val _isDone: Boolean
        get() {
            return commands.isEmpty()
        }

    /**
     * This property returns the combined list of all subsystems required by the scheduled commands.
     */
    override val requirements: List<Subsystem>
        get() {
            val temp = mutableListOf<Subsystem>()
            commands.forEach { command ->
                command.requirements.forEach { subsystem ->
                   if (!temp.contains(subsystem)) temp.add(subsystem)
               }
            }
            return temp
        }

    /**
     * This property returns true if all scheduled commands are interruptible.
     * If any command is not interruptible, this property returns false.
     */
    override val interruptible: Boolean
        get() = commands.all { it.interruptible }

    val commands = mutableListOf<Command>()

    /**
     * This function determines which commands should be run by comparing the value returned by the
     * provided value function with the first element in each outcome pair. Matching commands are
     * added to the list and started.
     */
    override fun onStart() {
        val invokedValue = value.invoke()
        outcomes.forEach {
            if (invokedValue == it.first && !commands.contains(it.second)) {
                commands.add(it.second)
                it.second.start()
            }
        }
    }

    /**
     * This function executes each scheduled command and checks if any of them have finished (isDone
     * is true). If a command has finished, it is ended and removed from the list of scheduled
     * commands.
     */
    override fun onExecute() {
        val toRemove = mutableListOf<Command>()
        commands.forEach {
            it.execute()
            if (it.isDone) {
                it.end(false)
                toRemove.add(it)
            }
        }
        toRemove.forEach {
            commands.remove(it)
        }
    }

    /**
     * This function calls the end() method on each scheduled command and clears the list of
     * commands to ensure no further execution.
     * @param interrupted this is true if the command was interrupted by another command using the
     *                    same Subsystem or the OpMode ending. It's false if the command ended
     *                    naturally (i.e. because isDone returned true).
     */
    override fun onEnd(interrupted: Boolean) {
        commands.forEach {
            it.end(interrupted)
        }
        commands.clear()
    }
}