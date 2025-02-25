package com.rowanmcalpin.nextftc.pedro

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.units.Angle
import com.rowanmcalpin.nextftc.core.units.abs
import com.rowanmcalpin.nextftc.core.units.deg

/**
 * A [Command] that turns the robot to a specified heading
 * @param targetHeading the heading to turn to
 * @param tolerance the maximum error between the current heading and the target heading required to finish
 * @throws FollowerNotInitializedException if the follower is not set
 */
class TurnTo(private val targetHeading: Angle, private val tolerance: Angle = 5.deg): Command() {
    override val isDone: Boolean
        get() = abs(targetHeading.normalized - Angle(PedroData.follower!!.totalHeading).normalized) <= tolerance.abs

    override fun start() {
        if (PedroData.follower == null) throw FollowerNotInitializedException()

        PedroData.follower!!.turnTo(targetHeading.inRad)
    }
}