package com.rowanmcalpin.nextftc.visualization

import com.rowanmcalpin.nextftc.CommandGroup
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.driving.drivers.Driver

data class MeepMeepRobot(
    val driver: Driver, val width: Double, val length: Double,
    val routine: () -> com.rowanmcalpin.nextftc.CommandGroup, val color: Constants.Color
)