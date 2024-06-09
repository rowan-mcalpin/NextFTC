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
package org.atomicrobotics3805.cflib.trajectories

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint

/**
 * This class is very similar to RoadRunner's TrajectoryBuilder, but it tracks the length of each
 * trajectory segment so that you can easily run commands after a certain segment is finished. Each
 * method doesn't have its own comment because they're all just methods that TrajectoryBuilder uses.
 */
@Suppress("unused")
class ParallelTrajectoryBuilder(val builder: TrajectoryBuilder) {

    private val segmentLengths = mutableListOf<Double>()

    fun build() = ParallelTrajectory(builder.build(), segmentLengths)

    fun lineTo(
        endPosition: Vector2d,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.lineTo(endPosition, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun lineToConstantHeading(
        endPosition: Vector2d,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.lineToConstantHeading(endPosition, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun lineToLinearHeading(
        endPose: Pose2d,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.lineToLinearHeading(endPose, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun lineToSplineHeading(
        endPose: Pose2d,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.lineToSplineHeading(endPose, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun strafeTo(
        endPosition: Vector2d,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.strafeTo(endPosition, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun forward(
        distance: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.forward(distance, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun back(
        distance: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.back(distance, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun strafeLeft(
        distance: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.strafeLeft(distance, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun strafeRight(
        distance: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.strafeRight(distance, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun splineTo(
        endPosition: Vector2d,
        endTangent: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.splineTo(endPosition, endTangent, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun splineToConstantHeading(
        endPosition: Vector2d,
        endTangent: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.splineToConstantHeading(endPosition, endTangent, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun splineToLinearHeading(
        endPose: Pose2d,
        endTangent: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.splineToLinearHeading(endPose, endTangent, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }

    fun splineToSplineHeading(
        endPose: Pose2d,
        endTangent: Double,
        velConstraintOverride: TrajectoryVelocityConstraint? = null,
        accelConstraintOverride: TrajectoryAccelerationConstraint? = null
    ): ParallelTrajectoryBuilder {
        builder.splineToSplineHeading(endPose, endTangent, velConstraintOverride, accelConstraintOverride)
        segmentLengths.add(builder.build().path.length())
        return this
    }
}