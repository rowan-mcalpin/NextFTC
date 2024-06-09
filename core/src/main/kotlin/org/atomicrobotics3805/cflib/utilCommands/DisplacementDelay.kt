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
package org.atomicrobotics3805.cflib.utilCommands

import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.util.epsilonEquals
import org.atomicrobotics3805.cflib.Constants.drive
import org.atomicrobotics3805.cflib.Command

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