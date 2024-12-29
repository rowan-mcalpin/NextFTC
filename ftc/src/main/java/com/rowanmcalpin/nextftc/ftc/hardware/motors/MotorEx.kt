package com.rowanmcalpin.nextftc.ftc.hardware.motors

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.ftc.OpModeData

class MotorEx(private val motor: DcMotorEx): Controllable {

    constructor(name: String): this(OpModeData.hardwareMap.get(DcMotorEx::class.java, name))

    override var currentPosition: Double
        get() = motor.currentPosition.toDouble()
        set(value) { }

    override var power: Double
        get() = motor.power
        set(value) { motor.power = value }
}