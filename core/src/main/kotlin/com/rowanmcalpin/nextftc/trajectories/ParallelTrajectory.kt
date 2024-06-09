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
package com.rowanmcalpin.nextftc.trajectories

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory

/**
 * This class is very similar to a RoadRunner's Trajectory, but it tracks the length of each
 * trajectory segment so that you can easily run commands after a certain segment is finished.
 */
data class ParallelTrajectory(val trajectory: Trajectory,
                              val segmentLengths: MutableList<Double>) {

    fun end(): Pose2d {
        return trajectory.end()
    }
}