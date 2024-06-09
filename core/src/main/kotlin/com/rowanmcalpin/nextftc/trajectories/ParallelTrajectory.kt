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