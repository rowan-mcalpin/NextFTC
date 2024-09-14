package com.rowanmcalpin.nextftc.command.utility

import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.CommandScheduler

/**
 * This command behaves as an `if` statement, and schedules commands based on the result of the if statement. It is
 * non-blocking, meaning `isDone` is true immediately, regardless of how long the scheduled commands take to run.
 */
class PassiveConditionalCommand(
    private val condition: () -> Boolean,
    private val trueCommand: Command,
    private val falseCommand: Command? = null): Command() {

    override val _isDone: Boolean
        get() = true

    override fun onStart() {
        if(condition.invoke()) {
            CommandScheduler.scheduleCommand(trueCommand)
        } else {
            if (falseCommand != null) {
                CommandScheduler.scheduleCommand(falseCommand)
            }
        }
    }
}

/**
 * This command behaves as an `if` statement, and schedules commands based on the result of the if statement. It is
 * blocking, meaning `isDone` will not return `true` until the scheduled commands have completed running.
 */
class BlockingConditionalCommand(
    private val condition: () -> Boolean,
    private val trueCommand: Command,
    private val falseCommand: Command? = null): Command() {

    private var result: Boolean? = null;
    override val _isDone: Boolean
        get() {
            if (result == null) {
                return false
            }
            if (result!!) {
                return trueCommand.isDone
            }
            return falseCommand?.isDone == true
        }

    override fun onStart() {
        if(condition.invoke()) {
            CommandScheduler.scheduleCommand(trueCommand)
            result = true
        } else {
            if (falseCommand != null) {
               CommandScheduler.scheduleCommand(falseCommand)
            }
            result = false
        }
    }
}