package com.rowanmcalpin.nextftc.utilCommands

class ConditionalCommand(
    private val condition: () -> Boolean,
    private val trueOperation: () -> Unit,
    private val falseOperation: () -> Unit = { }) : com.rowanmcalpin.nextftc.Command() {

    override val _isDone: Boolean
        get() = true

    override fun start() {
        if(condition.invoke()) {
            trueOperation.invoke()
        } else {
            falseOperation.invoke()
        }
    }
}
