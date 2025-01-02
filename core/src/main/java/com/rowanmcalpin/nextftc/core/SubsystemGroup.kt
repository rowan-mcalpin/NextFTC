package com.rowanmcalpin.nextftc.core

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup

open class SubsystemGroup(vararg val subsystems: Subsystem): Subsystem() {
    override val defaultCommand: () -> Command
        get() = { ParallelGroup(*subsystems.map { it.defaultCommand.invoke() }.toTypedArray()) }

    override fun initialize() {
        subsystems.forEach {
            it.initialize()
        }
    }

    override fun periodic() {
        subsystems.forEach {
            it.periodic()
        }
    }
}