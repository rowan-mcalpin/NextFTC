package com.rowanmcalpin.nextftc.ftc.hardware.controllables

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.rowanmcalpin.nextftc.ftc.OpModeData

/**
 * Wrapper class for motors that implements controllable (and can therefore be used with RunToPosition
 * commands).
 */
class MotorEx(val motor: DcMotorEx): Controllable {

    constructor(name: String): this(OpModeData.hardwareMap.get(DcMotorEx::class.java, name))

    var direction: Direction
        get() = motor.direction
        set(value) { motor.direction = value }

    override var currentPosition: Double
        get() = motor.currentPosition.toDouble()
        set(value) { }

    override var velocity: Double
        get() = motor.velocity
        set(value) { }

    override var power: Double
        get() = motor.power
        set(value) {
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            motor.power = value
        }
}