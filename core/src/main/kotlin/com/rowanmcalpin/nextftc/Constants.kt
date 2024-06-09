package com.rowanmcalpin.nextftc

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.rowanmcalpin.nextftc.driving.drivers.Driver

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