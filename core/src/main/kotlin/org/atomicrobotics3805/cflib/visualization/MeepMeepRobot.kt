package org.atomicrobotics3805.cflib.visualization

import org.atomicrobotics3805.cflib.CommandGroup
import org.atomicrobotics3805.cflib.Constants
import org.atomicrobotics3805.cflib.driving.drivers.Driver

data class MeepMeepRobot(
    val driver: Driver, val width: Double, val length: Double,
    val routine: () -> CommandGroup, val color: Constants.Color
)