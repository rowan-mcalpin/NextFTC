/*
    Copyright (c) 2022 Atomic Robotics (https://atomicrobotics3805.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package org.atomicrobotics3805.cflib

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.atomicrobotics3805.cflib.driving.drivers.Driver

/**
 * This object contains various "constant" values used by other classes. This object is not strictly
 * necessary, but without it, we'd have to pass around the same few variables to countless different
 * places. The word "constant" is used lightly because the variables in this class are not constants
 * in the strict sense. They can change between OpModes, but stay the same during each OpMode. The
 * first thing that an OpMode should do is assign values to these variables. Otherwise, many classes
 * will break.
 */
object Constants {

    /**
     * Alliance color, this has to be specified in Auto programs
     */
    enum class Color {
        BLUE,
        RED,
        UNKNOWN
    }

    object Side {
        val LEFT : Color
            get() {
                return Color.BLUE
            }

        val RIGHT : Color
            get() {
                return Color.RED
            }
    }

    val leftSide: Boolean
        get() = (color == Side.LEFT)

    var color = Color.UNKNOWN
    lateinit var opMode: LinearOpMode
    lateinit var drive: Driver
    var endPose: Pose2d? = null
}