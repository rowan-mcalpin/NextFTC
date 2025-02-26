package com.rowanmcalpin.nextftc.pedro

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.units.Angle
import com.rowanmcalpin.nextftc.core.units.abs
import com.rowanmcalpin.nextftc.core.units.deg

/**
 * A [Command] that turns the robot by an angle
 * @param angle the angle to turn by. Positive angles turn counterclockwise and negative angles turn clockwise.
 * @param tolerance the maximum error between the current heading and the target heading required to finish
 * @throws FollowerNotInitializedException if the follower is not set
 */
class Turn @JvmOverloads constructor(private val angle: Angle, private val tolerance: Angle = 5.deg): Command() {
    private lateinit var targetHeading: Angle

    override val isDone: Boolean
        get() = abs(targetHeading.normalized - Angle.fromRad(PedroData.follower!!.totalHeading).normalized) <= tolerance.abs

    override fun start() {
        if (PedroData.follower == null) throw FollowerNotInitializedException()

        targetHeading = Angle.fromRad(PedroData.follower!!.totalHeading) + angle
        PedroData.follower!!.turn(angle.abs.inRad, angle.sign > 0)
    }
}