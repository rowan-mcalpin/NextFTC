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
package org.atomicrobotics3805.cflib.trajectories

/**
 * This class contains all the RoadRunner trajectories and start positions in the project. It's
 * used in routines. You can find how to use each of the possible trajectory segments (like back and =
 * splineToSplineHeading) here: https://learnroadrunner.com/trajectorybuilder-functions.html
 * The coordinate system for Road Runner is as follows:
 * Positive x is up, negative x is down, positive y is right, negative y is left, positive angles
 * are counterclockwise. All measurements are in inches and radians.
 */
abstract class TrajectoryFactory {

    var initialized = false

    open fun initialize() {
        initialized = true
    }
}