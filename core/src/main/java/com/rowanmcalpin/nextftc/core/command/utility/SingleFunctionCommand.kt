package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.command.Command

open class SingleFunctionCommand(val action: () -> Boolean) : Command() {

    constructor(): this({false})

    private var _isDone = true

    override val isDone: Boolean
        get() = _isDone

    override fun update() {
        _isDone = !run()
    }

    open fun run(): Boolean {
        return action()
    }
}