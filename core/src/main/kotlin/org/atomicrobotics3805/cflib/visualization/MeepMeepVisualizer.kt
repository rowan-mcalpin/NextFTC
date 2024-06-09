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
package org.atomicrobotics3805.cflib.visualization

import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.trajectorysequence.sequencesegment.SequenceSegment
import com.noahbres.meepmeep.roadrunner.trajectorysequence.sequencesegment.TrajectorySegment
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence
import org.atomicrobotics3805.cflib.CommandGroup
import org.atomicrobotics3805.cflib.Constants
import org.atomicrobotics3805.cflib.driving.DriveConstants
import org.atomicrobotics3805.cflib.driving.FollowTrajectory
import org.atomicrobotics3805.cflib.driving.drivers.Driver
import org.atomicrobotics3805.cflib.trajectories.TrajectoryFactory

object MeepMeepVisualizer {

    val robots = mutableListOf<MeepMeepRobot>()

    fun run(trajectoryFactory: TrajectoryFactory, windowSize: Int = 600, darkMode: Boolean = true, backgroundAlpha: Float = 0.95f, background: MeepMeep.Background = MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK) {
        val meepMeep = MeepMeep(windowSize)
        meepMeep.setBackground(background)
            .setDarkMode(darkMode)
            .setBackgroundAlpha(backgroundAlpha)
        robots.forEach {
            Constants.drive = it.driver
            Constants.color = it.color
            trajectoryFactory.initialize()
            val constants: DriveConstants = it.driver.constants
            val botBuilder: DefaultBotBuilder = DefaultBotBuilder(meepMeep)
                .setDimensions(it.width, it.length)
                .setConstraints(
                    constants.MAX_VEL, constants.MAX_ACCEL,
                    constants.MAX_ANG_VEL, constants.MAX_ANG_ACCEL,
                    constants.TRACK_WIDTH
                ).setColorScheme(
                    if (it.color == Constants.Color.RED) ColorSchemeRedDark()
                    else ColorSchemeBlueDark()
                )
            meepMeep.addEntity(botBuilder.followTrajectorySequence(
                TrajectorySequence(
                    routineToSegmentList(it.routine.invoke())
                )
            ))
        }
        meepMeep.start()
    }

    fun addRobot(robot: MeepMeepRobot) {
        robots.add(robot)
    }

    private fun routineToSegmentList(routine: CommandGroup): List<SequenceSegment> {
        val trajectories = arrayListOf<SequenceSegment>()
        for (command in routine.commands) {
            if (command is FollowTrajectory) {
                trajectories.add(TrajectorySegment(command.trajectory.trajectory))
            }
            if (command is CommandGroup) {
                trajectories.addAll(routineToSegmentList(command))
            }
        }
        return trajectories
    }
}