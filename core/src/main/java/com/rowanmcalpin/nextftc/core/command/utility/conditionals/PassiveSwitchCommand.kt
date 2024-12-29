package com.rowanmcalpin.nextftc.core.command.utility.conditionals

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * This behaves like the command-form of a switch statement. You provide it with a value to
 * reference, and a list of options of outcomes. It is non-blocking, meaning `isDone` is true 
 * immediately, regardless of how long the scheduled command(s) take to run.
 * @param value the value to reference
 * @param outcomes all of the options for outcomes
 * @param default the command to schedule if none of the outcomes are fulfilled
 */
class PassiveSwitchCommand(
    private val value: () -> Any,
    private vararg val outcomes: Pair<Any, () -> Command>,
    private val default: (() -> Command)? = null
): Command() {

    /**
     * The commands that have been selected
     */
    private val selectedCommands: MutableList<Command> = mutableListOf()

    override val isDone: Boolean
        get() = true
    
    override fun start() {
        val value = this.value.invoke()
        outcomes.forEach {
            if (it.first == value) {
                selectedCommands.add(it.second.invoke())
                CommandManager.scheduleCommand(selectedCommands.last())
            }
        }
        
        if (selectedCommands.size == 0) {
            if (default != null) {
                selectedCommands.add(default.invoke())
                CommandManager.scheduleCommand(selectedCommands.last())
            }
        }
    }
    
}