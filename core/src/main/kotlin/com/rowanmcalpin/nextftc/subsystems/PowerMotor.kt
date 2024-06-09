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

import com.qualcomm.robotcore.hardware.DcMotor
import com.rowanmcalpin.nextftc.Command
import com.qualcomm.robotcore.util.RobotLog
import com.rowanmcalpin.nextftc.hardware.MotorEx

/**
 * This command powers a motor and sets it to a certain mode if one is supplied.
 *
 * @param motor the motor to power
 * @param power the power (-1.0 to 1.0) to set it to
 * @param mode the RunMode that the motor should be set to, if any
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 */
class PowerMotor(
    private val motor: MotorEx,
    private val power: Double,
    private val mode: DcMotor.RunMode? = null,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true,
    private val logData: Boolean = false
) : com.rowanmcalpin.nextftc.Command() {

    override fun start() {
        if (mode != null) {
            motor.mode = mode
        }
        motor.power = power
        if(logData) {
            RobotLog.i("PowerMotor", power)
        }
    }
}