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
package org.atomicrobotics3805.cflib.utilCommands

import com.qualcomm.robotcore.util.ElapsedTime
import org.atomicrobotics3805.cflib.Command

/**
 * This class is a simple delay that waits for a certain amount of time. Like other delays, it
 * should be put in a sequential command group before the command that needs to be delayed. A delay in a parallel 
 * command group will accomplish nothing except taking up memory on the device.
 */
@SuppressWarnings("unused")
class Delay(private val time: Double): Command() {
    override val _isDone: Boolean
        get() = timer.seconds() > time

    private val timer = ElapsedTime()

    /**
     * Resets timer
     */
    override fun start() {
        timer.reset()
    }
}