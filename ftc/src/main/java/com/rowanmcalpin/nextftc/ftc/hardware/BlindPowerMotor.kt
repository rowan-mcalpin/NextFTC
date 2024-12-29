package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command

@Deprecated("Deprecated in favor of controllables", replaceWith = ReplaceWith("SetPower"))
/**
 * Sets a motor to a specific power without any internal feedback
 * 
 * @param motor the motor to control
 * @param power the power to set the motor to
 * @param subsystems the subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class BlindPowerMotor(val motor: DcMotorEx, val power: Double, override val subsystems: Set<Subsystem>): Command() {
    
    constructor(motor: DcMotorEx, power: Double, subsystem: Subsystem): this(motor, power, setOf(subsystem))
    
    override val isDone: Boolean
        get() = true

    override fun start() {
        motor.power = power
    }
}