package com.rowanmcalpin.nextftc.pedro

import com.pedropathing.localization.Pose
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.square
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * This is a delay that waits until the robot is within [distanceTolerance] units of the specified [point].
 *
 * @param point the point to wait until the robot is near
 * @param distanceTolerance how close to the point the robot must be for it to be considered "at" the point
 */
class ProximityDelay @JvmOverloads constructor(val point: Pose, val distanceTolerance: Double = 4.0): Command() {
    override val isDone: Boolean
        get() {
            if (PedroData.follower == null) {
                throw FollowerNotInitializedException()
            }

            return PoseUtil.getDistance(point, PedroData.follower!!.pose) <= distanceTolerance
        }
}