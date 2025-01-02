package com.rowanmcalpin.nextftc.core

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup

open class SubsystemGroup(vararg val subsystems: Subsystem): Subsystem()