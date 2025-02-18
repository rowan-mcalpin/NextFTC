/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rowanmcalpin.nextftc.pedro

import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.util.Constants
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.components.NextComponent

/**
 * This component adds PedroPathing to your OpMode. It automatically sets the constants and
 * instantiates the follower, which
 */
class PedroComponent(val fConstants: Class<*>, val lConstants: Class<*>, val startingPose: Pose, val resetIMU: Boolean = false): NextComponent {
    override fun preInit() {
        if (OpModeData.hardwareMap == null) {
            throw UninitializedPropertyAccessException("hardwareMap has not been initialized")
        }
        Constants.setConstants(fConstants, lConstants)
        PedroData.follower = Follower(OpModeData.hardwareMap)
        if (resetIMU) {
            // TODO RESET IMU
        }
        PedroData.follower!!.setStartingPose(startingPose)
    }

    override fun preStartButtonPressed() {
        CommandManager.scheduleCommand(UpdateFollower())
    }
}