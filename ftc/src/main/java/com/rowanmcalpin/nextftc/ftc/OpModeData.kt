package com.rowanmcalpin.nextftc.ftc

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import com.pedropathing.follower.Follower

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
    
    var follower: Follower? = null
    
    var gamepad1: Gamepad? = null
    var gamepad2: Gamepad? = null

    lateinit var alliance: Alliance
    lateinit var opModeType: OpModeType
}