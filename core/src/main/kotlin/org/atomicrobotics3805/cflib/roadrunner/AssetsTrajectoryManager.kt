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
package org.atomicrobotics3805.cflib.roadrunner

import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigManager.loadGroupConfig
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigManager.loadConfig
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryGroupConfig
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfig
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigManager.GROUP_FILENAME
import java.io.IOException

/**
 * Set of utilities for loading trajectories from assets (the plugin save location).
 */
object AssetsTrajectoryManager {
    /**
     * Loads the group config.
     */
    fun loadGroupConfig(): TrajectoryGroupConfig? {
        return try {
            val inputStream = AppUtil.getDefContext().assets.open(
                "trajectory/$GROUP_FILENAME"
            )
            loadGroupConfig(inputStream)
        } catch (e: IOException) {
            null
        }
    }

    /**
     * Loads a trajectory config with the given name.
     */
    fun loadConfig(name: String): TrajectoryConfig? {
        return try {
            val inputStream = AppUtil.getDefContext().assets.open(
                "trajectory/$name.yaml"
            )
            loadConfig(inputStream)
        } catch (e: IOException) {
            null
        }
    }

    /**
     * Loads a trajectory builder with the given name.
     */
    fun loadBuilder(name: String): TrajectoryBuilder? {
        val groupConfig = loadGroupConfig()
        val config = loadConfig(name)
        return if (groupConfig == null || config == null) {
            null
        } else config.toTrajectoryBuilder(groupConfig)
    }

    /**
     * Loads a trajectory with the given name.
     */
    fun load(name: String): Trajectory? {
        val builder = loadBuilder(name) ?: return null
        return builder.build()
    }
}