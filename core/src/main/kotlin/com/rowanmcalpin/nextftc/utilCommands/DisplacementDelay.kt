package com.rowanmcalpin.nextftc.utilCommands

import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.util.epsilonEquals
import com.rowanmcalpin.nextftc.Constants.drive
import com.rowanmcalpin.nextftc.Command

/**
 * This class is a delay that waits until the robot has driven either a certain number of inches or
 * a certain number of trajectory segments. Like other delays, it should be put in a sequential
 * command group before the command that needs to be delayed. A delay in a parallel command group will accomplish
 * nothing except taking up memory on the device.
 *
 * @param displacement the distance the robot needs to travel along its current trajectory in inches
 */
@Suppress("Unused")
class DisplacementDelay(private val displacement: Double): Command() {

    override val _isDone: Boolean
        get() = displacementToTime(drive.follower.trajectory.profile, displacement) < drive.follower.elapsedTime()

    /**
     * @param segmentNumber the number of segments in the current trajectory that the robot has to
     *                      follow
     */
    constructor(segmentNumber: Int) : this(drive.trajectory?.segmentLengths?.get(segmentNumber) ?: 0.0)

    /**
     * Converts a given displacement into an amount of time
     * @param profile the motion profile that the robot is using
     * @param s the displacement in inches
     */
    private fun displacementToTime(profile: MotionProfile, s: Double): Double {
        var tLo = 0.0
        var tHi = profile.duration()
        while (!(tLo epsilonEquals tHi)) {
            val tMid = 0.5 * (tLo + tHi)
            if (profile[tMid].x > s) {
                tHi = tMid
            } else {
                tLo = tMid
            }
        }
        return 0.5 * (tLo + tHi)
    }
}