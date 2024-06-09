package com.rowanmcalpin.nextftc.trajectories

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