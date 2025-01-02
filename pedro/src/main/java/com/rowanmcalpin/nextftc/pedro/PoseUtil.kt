package com.rowanmcalpin.nextftc.pedro

import com.pedropathing.localization.Pose
import com.rowanmcalpin.nextftc.core.square
import kotlin.math.abs
import kotlin.math.sqrt

object PoseUtil {
    @JvmStatic
    fun getDistance(a: Pose, b: Pose): Double {
        return sqrt(abs(b.x - a.x).square + abs(b.y - a.y).square)
    }
}