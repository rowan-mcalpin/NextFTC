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
package com.rowanmcalpin.nextftc.subsystems

/**
 * A subsystem represents any mechanism on the robot that cannot be used by two things at once. It
 * serves two purposes: first, the CommandScheduler prevents more than one command from using a
 * subsystem at a time, and second, the CommandScheduler runs the periodic and inUsePeriodic
 * functions every loop.
 *
 * P.S. I know that the name of an interface is supposed to be an adjective, so if someone could
 * help me turn the word subsystem into an adjective that would be greatly appreciated.
 */
interface Subsystem {

    /**
     * The CommandScheduler runs this function when the Subsystem is registered using
     * registerSubsystems(). It should contain anything that needs to be done once at the start of
     * the program, like initializing motors or servos.
     */
    fun initialize() { }

    /**
     * The CommandScheduler runs this function every loop (provided the subsystem has been
     * registered using registerSubsystems()). It should contain anything related to the subsystem
     * that needs to be done repeatedly, such as displaying telemetry information.
     */
    fun periodic() { }

    /**
     * This function is similar to periodic, but it's only run if there's an active command using
     * this subsystem.
     */
    fun inUsePeriodic() { }
}