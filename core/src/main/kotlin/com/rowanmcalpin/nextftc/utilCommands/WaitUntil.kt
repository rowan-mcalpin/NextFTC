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
package com.rowanmcalpin.nextftc.utilCommands

import com.rowanmcalpin.nextftc.Command

/**
 * This class is a type of delay that doesn't finish until a given check returns true. Like other
 * delays, it should be put in a sequential command group before the command that needs to be
 * delayed.
 */
class WaitUntil(private val check: () -> Boolean) : com.rowanmcalpin.nextftc.Command() {

    override val _isDone: Boolean
        get() = check.invoke()
}