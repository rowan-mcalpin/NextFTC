package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import kotlin.math.abs

@Deprecated("Removed in favor of controllables", replaceWith = ReplaceWith("RunToPosition"))
/**
 * This command holds a motor's position until another command is scheduled that uses the same
 * subsystem.
 * @param motor the motor to control
 * @param controller the controller to implement
 * @param subsystems the list of subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MotorHoldPosition(val motor: DcMotorEx, val controller: PIDFController,
                        override val subsystems: Set<Subsystem>): Command() {
    override val isDone = false
    
    constructor(motor: DcMotorEx, controller: PIDFController, subsystem: Subsystem): this(motor, controller, setOf(subsystem))

    override fun start() {
        controller.target = motor.currentPosition.toDouble()
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