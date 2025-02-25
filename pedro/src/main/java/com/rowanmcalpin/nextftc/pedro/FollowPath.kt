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

import com.pedropathing.pathgen.Path
import com.pedropathing.pathgen.PathChain
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.Drivetrain

/**
 * This Command tells the PedroPath follower to follow a specific path or pathchain
 * @param path the path to follow
 * @param holdEnd whether to actively hold position after the path is done being followed
 * @param maxPower the max power, between 0 and 1. If maxPower is null then the default (set in `FConstants` or with `follower.setMaxPower`) is used.
 * @throws FollowerNotInitializedException if the follower is not set
 * @throws IllegalArgumentException if maxPower is not null or in the interval [0, 1]
 */
class FollowPath @JvmOverloads constructor(private val path: PathChain, private val holdEnd: Boolean = false, private val maxPower: Double? = null): Command() {
    
    @JvmOverloads constructor(path: Path, holdEnd: Boolean = false, maxPower: Double? = null): this(PathChain(path), holdEnd, maxPower)
    
    override val isDone: Boolean
        get() = !PedroData.follower!!.isBusy

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    override fun start() {
        if (PedroData.follower == null) throw FollowerNotInitializedException()
        require(maxPower == null || maxPower in 0.0..1.0) { "maxPower must be null or between 0 and 1" }

        if (maxPower == null)
            PedroData.follower!!.followPath(path, holdEnd)
        else
            PedroData.follower!!.followPath(path, maxPower, holdEnd)
    }
}