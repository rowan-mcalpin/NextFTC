package com.rowanmcalpin.nextftc.command.utility

import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.CommandScheduler

class OptionCommand(
    private val value: () -> Any,
    private vararg val outcomes: Pair<Any, () -> Unit>
) : Command() {
    constructor(
        differentSignature: Any,
        value: () -> Any,
        vararg outcomes: Pair<Any, Command>) : this(value, *split(outcomes).toTypedArray())
    companion object {
        private fun split(before: Array<out Pair<Any, Command>>) : List<Pair<Any, () -> Unit>> {
            val newList: MutableList<Pair<Any, () -> Unit>> = mutableListOf()

            before.forEach {
                newList.add(Pair(it.first) { CommandScheduler.scheduleCommand(it.second) })
            }
            return newList
        }
    }

    override val _isDone: Boolean
        get() = true

    override fun onStart() {
        val invokedValue: Any = value.invoke()
        outcomes.forEach {
            if(invokedValue == it.first) {
                it.second.invoke()
            }
        }
    }
}