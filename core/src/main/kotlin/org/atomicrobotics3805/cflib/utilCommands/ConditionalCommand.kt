package org.atomicrobotics3805.cflib.utilCommands

import org.atomicrobotics3805.cflib.Command

class ConditionalCommand(
    private val condition: () -> Boolean,
    private val trueOperation: () -> Unit,
    private val falseOperation: () -> Unit = { }) : Command() {

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
