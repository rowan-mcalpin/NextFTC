package com.rowanmcalpin.nextftc.core.command.utility.conditionals

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.CommandManager

/**
 * This command behaves as an `if` statement, and adds commands based on the result of the if 
 * statement. It is non-blocking, meaning `isDone` is true immediately, regardless of how long the 
 * scheduled command takes to run.
 * @param condition the condition to reference
 * @param trueCommand the command to schedule if the reference is true
 * @param falseCommand the command to schedule if the reference is false
 */
class PassiveConditionalCommand(
    private val condition: () -> Boolean,
    private val trueCommand: () -> Command,
    private val falseCommand: (() -> Command)? = null
): Command() {
    override val isDone: Boolean
        get() = true
    
    override fun start() {
        if (condition.invoke()) {
            CommandManager.scheduleCommand(trueCommand.invoke())
        } else {
            if (falseCommand != null) {
                CommandManager.scheduleCommand(falseCommand.invoke())
            }
        }
    }
}