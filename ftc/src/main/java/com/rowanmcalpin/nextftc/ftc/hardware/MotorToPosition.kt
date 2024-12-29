package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import kotlin.math.abs

/**
 * This implements a PID controller to drive a motor to a specified target position.
 * 
 * @param motor the motor to control
 * @param target the target position
 * @param controller the controller to implement
 * @param subsystems the list of subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MotorToPosition(val motor: DcMotorEx, val target: Double, val controller: PIDFController,
                      override val subsystems: Set<Subsystem>): Command() {
    
    constructor(motor: DcMotorEx, target: Double, controller: PIDFController, subsystem: Subsystem): this(motor, target, controller, setOf(subsystem))
                          
    override val isDone: Boolean
        get() = controller.atSetPoint(motor.currentPosition.toDouble())

    override fun start() {
        controller.target = target
        controller.reset()
    }

    override fun update() {
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        val calculatedPower = controller.calculate(motor.currentPosition.toDouble())
        if (abs(motor.power - calculatedPower) > 0.001) {
            motor.power = calculatedPower
        }
    }
}