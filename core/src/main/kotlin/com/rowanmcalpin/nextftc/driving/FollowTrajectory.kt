package com.rowanmcalpin.nextftc.driving

import com.acmerobotics.roadrunner.drive.DriveSignal
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.Constants.drive
import com.rowanmcalpin.nextftc.subsystems.Subsystem
import com.rowanmcalpin.nextftc.trajectories.ParallelTrajectory

/**
 * This command follows a preexisting trajectory.
 *
 * @param trajectory the trajectory to follow
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 */
@Suppress("unused")
class FollowTrajectory(
    val trajectory: ParallelTrajectory,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true
) : Command() {

    override val _isDone: Boolean
        get() = !drive.follower.isFollowing()

    /**
     * Tells the follower to start following the trajectory.
     */
    override fun onStart() {
        drive.follower.followTrajectory(trajectory.trajectory)
        drive.trajectory = trajectory
    }

    /**
     * Updates the drive signal
     */
    override fun onExecute() {
        drive.setDriveSignal(drive.follower.update(drive.poseEstimate))
    }

    override fun onEnd(interrupted: Boolean) {
        drive.setDriveSignal(DriveSignal())
        drive.trajectory = null
    }
}