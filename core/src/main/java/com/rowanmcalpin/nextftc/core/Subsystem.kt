/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 *     Copyright (C) 2025 Rowan McAlpin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.rowanmcalpin.nextftc.core

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand

/**
 * A [Subsystem] represents a real-world system (such as a lift, intake, or claw) that cannot be
 * controlled by multiple commands simultaneously.
 */
abstract class Subsystem {
    open val defaultCommand: Command get() = NullCommand()

    /**
     * Initializes this subsystem. This function is perfect for calling `hardwareMap.get` or
     * otherwise initializing hardware devices. 
     */
    open fun initialize() { }

    /**
     * This function is called every update.
     */
    open fun periodic() { }
}