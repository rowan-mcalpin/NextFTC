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

package com.rowanmcalpin.nextftc.ftc

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

object OpModeData {

    enum class Alliance {
        RED,
        BLUE,
        NONE
    }

    enum class OpModeType {
        TELEOP,
        AUTO,
        NONE
    }

    lateinit var hardwareMap: HardwareMap
    
    lateinit var opMode: OpMode

    lateinit var telemetry: Telemetry
    
    var gamepad1: Gamepad? = null
    var gamepad2: Gamepad? = null

    lateinit var alliance: Alliance
    lateinit var opModeType: OpModeType
}