package com.rowanmcalpin.nextftc.visualization

import com.rowanmcalpin.nextftc.command.groups.CommandGroup
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.driving.drivers.Driver

data class MeepMeepRobot(
    val driver: Driver, val width: Double, val length: Double,
    val routine: () -> CommandGroup, val color: Constants.Color
)