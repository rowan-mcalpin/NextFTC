package com.rowanmcalpin.nextftc.command.utility

import com.rowanmcalpin.nextftc.command.Command

class ConditionalCommand(
    private val condition: () -> Boolean,
    private val trueOperation: () -> Unit,
    private val falseOperation: () -> Unit = { }) : Command() {

    override val _isDone: Boolean
        get() = true

    override fun onStart() {
        if(condition.invoke()) {
            trueOperation.invoke()
        } else {
            falseOperation.invoke()
        }
    }
}
