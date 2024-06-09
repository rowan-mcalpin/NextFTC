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
package org.atomicrobotics3805.cflib.driving

import com.acmerobotics.roadrunner.drive.DriveSignal
import org.atomicrobotics3805.cflib.Command
import org.atomicrobotics3805.cflib.Constants.drive
import org.atomicrobotics3805.cflib.subsystems.Subsystem
import org.atomicrobotics3805.cflib.trajectories.ParallelTrajectory

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
    override fun start() {
        drive.follower.followTrajectory(trajectory.trajectory)
        drive.trajectory = trajectory
    }

    /**
     * Updates the drive signal
     */
    override fun execute() {
        drive.setDriveSignal(drive.follower.update(drive.poseEstimate))
    }

    override fun end(interrupted: Boolean) {
        drive.setDriveSignal(DriveSignal())
        drive.trajectory = null
    }
}