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
package com.rowanmcalpin.nextftc.driving

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.rowanmcalpin.nextftc.roadrunner.AxisDirection
import com.qualcomm.robotcore.hardware.PIDFCoefficients

/**
 * This contains all of the constants used in every kind of drivetrain (tank, Mecanum, swerve, etc.)
 */
@Suppress("PropertyName")
interface DriveConstants {

    val TICKS_PER_REV: Double
    val MAX_RPM: Double
    val MOTOR_VEL_PID: PIDFCoefficients
    val IS_RUN_USING_ENCODER: Boolean
    val kV: Double
    val kA: Double
    val kStatic: Double
    val WHEEL_RADIUS: Double
    val GEAR_RATIO: Double
    val TRACK_WIDTH: Double
    val MAX_VEL: Double
    val MAX_ACCEL: Double
    val MAX_ANG_VEL: Double
    val MAX_ANG_ACCEL: Double
    val TRANSLATIONAL_PID: PIDCoefficients
    val HEADING_PID: PIDCoefficients
    val DRIVER_SPEEDS: List<Double>
    /**
    Depending on the direction your hub is mounted, the IMU has to be reset to have a different vertical direction.
    //                           | Z axis
    //                           |
    //     (Motor Port Side)     |   / X axis
    //                       ____|__/____
    //          Y axis     / *   | /    /|   (IO Side)
    //          _________ /______|/    //      I2C
    //                   /___________ //     Digital
    //                  |____________|/      Analog
    //
    //                 (Servo Port Side)
    //
    // The positive x axis points toward the USB port(s)
    */
    val VERTICAL_AXIS: AxisDirection

    /**
     * Converts encoder ticks to inches
     */
    fun encoderTicksToInches(ticks: Double): Double {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV
    }

    /**
     * Converts revs per minute to inches per second
     */
    fun rpmToVelocity(rpm: Double): Double {
        return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0
    }

    /**
     * Automatically calculates the fixed feedforward constant for the motor velocity
     */
    fun getMotorVelocityF(ticksPerSecond: Double): Double {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 / ticksPerSecond
    }
}