@file:JvmName("PoseUtil")
package com.rowanmcalpin.nextftc.pedro

import com.pedropathing.localization.Pose
import kotlin.math.pow
import kotlin.math.sqrt

fun Pose.distanceFrom(other: Pose): Double = sqrt((other.x - this.x).pow(2) + (other.y - this.y).pow(2))
