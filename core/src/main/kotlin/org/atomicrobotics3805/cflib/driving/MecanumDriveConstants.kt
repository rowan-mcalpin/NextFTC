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
package org.atomicrobotics3805.cflib.driving

import org.atomicrobotics3805.cflib.hardware.MotorEx

@Suppress("PropertyName")
interface MecanumDriveConstants : DriveConstants {

    val LATERAL_MULTIPLIER: Double
    val DRIFT_MULTIPLIER: Double
    val DRIFT_TURN_MULTIPLIER: Double
    val BACKWARD_DRIFT_MULTIPLIER: Double
    val RIGHT_DRIFT_MULTIPLIER: Double
    val LEFT_FRONT_MOTOR: MotorEx
    val RIGHT_FRONT_MOTOR: MotorEx
    val LEFT_BACK_MOTOR: MotorEx
    val RIGHT_BACK_MOTOR: MotorEx
    val POV: DriverControlled.POV
    val REVERSE_STRAFE: Boolean
    val REVERSE_STRAIGHT: Boolean
    val REVERSE_TURN: Boolean

}