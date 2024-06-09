package com.rowanmcalpin.nextftc.roadrunner

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