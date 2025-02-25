package com.rowanmcalpin.nextftc.pedro

import com.pedropathing.localization.Pose
import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This is a delay that waits until the robot has been displaced (by driving) a certain distance.
 * This does not use the length of the path; if you are driving in circles smaller than your distance,
 * it will never finish.
 *
 * @param distance the distance the robot must drive
 */
class DisplacementDelay(private val distance: Double): Command() {
    private lateinit var startPosition: Pose

    override val isDone: Boolean
        get() {
            if (PedroData.follower == null) throw FollowerNotInitializedException()

            return startPosition.distanceFrom(PedroData.follower!!.pose) >= distance
        }

    override fun start() {
        if (PedroData.follower == null) throw FollowerNotInitializedException()

        startPosition = PedroData.follower!!.pose
    }
}