package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import kotlin.math.abs

@Deprecated("Deprecated in favor of controllables", ReplaceWith("RunToVelocity"))
/**
 * This implements a PID controller to drive a motor to a specified target velocity.
 *
 * @param motor the motor to control
 * @param targetVelocity the target velocity
 * @param controller the controller to implement
 * @param subsystems the list of subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 * @param outCondition will be evaluated every update, and the command will stop once it returns true
 */
class MotorToVelocity(val motor: DcMotorEx, val targetVelocity: Double,
                      val controller: PIDFController, override val subsystems: Set<Subsystem>,
                      val outCondition: () -> Boolean = { abs(motor.velocity)-targetVelocity < 10 }): Command() {
                          
    constructor(motor: DcMotorEx, targetVelocity: Double, controller: PIDFController, subsystem: Subsystem, outCondition: () -> Boolean = { abs(motor.velocity)-targetVelocity < 10 }): this(motor, targetVelocity, controller, setOf(subsystem), outCondition)
                          
    // Java compatability constructors
    constructor(motor: DcMotorEx, targetVelocity: Double, controller: PIDFController, subsystems: Set<Subsystem>): this(motor, targetVelocity, controller, subsystems, { abs(motor.velocity)-targetVelocity < 10 })
    constructor(motor: DcMotorEx, targetVelocity: Double, controller: PIDFController, subsystem: Subsystem): this(motor, targetVelocity, controller, setOf(subsystem), { abs(motor.velocity)-targetVelocity < 10 })
    
    override val isDone: Boolean
        get() = outCondition.invoke()

    override fun start() {
        controller.target = targetVelocity
        controller.reset()
    }

    override fun update() {
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motor.power = controller.calculate(motor.velocity)
    }
}