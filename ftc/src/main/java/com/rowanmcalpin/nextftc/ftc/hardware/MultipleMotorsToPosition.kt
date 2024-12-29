package com.rowanmcalpin.nextftc.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import kotlin.math.abs

/**
 * This implements a PID controller to drive multiple motors to a specified target position.
 * 
 * @param motorsAndControllers a map of all motors and their corresponding controllers
 * @param target the target position
 * @param subsystems the list of subsystems this command interacts with (should be whatever
 *                      subsystem holds this command)
 */
class MultipleMotorsToPosition(val motorsAndControllers: Map<DcMotorEx, PIDFController>, val target: Double,
                               override val subsystems: Set<Subsystem>): Command() {
                                   
    constructor(motorsAndControllers: Map<DcMotorEx, PIDFController>, target: Double, subsystem: Subsystem): this (motorsAndControllers, target, setOf(subsystem))
    
    override val isDone: Boolean
        get() = motorsAndControllers.all { it.value.atSetPoint(it.key.currentPosition.toDouble()) }

    override fun start() {
        if (motorsAndControllers.isEmpty()) {
            throw IllegalArgumentException("You must specify at least one pair of motors and controllers")
        }
        
        motorsAndControllers.forEach {
            it.value.target = target
            it.value.reset()
        }
    }

    override fun update() {
        motorsAndControllers.forEach {
            it.key.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            val calculatedPower = it.value.calculate(it.key.currentPosition.toDouble())
            if (abs(it.key.power - calculatedPower) > 0.001) {
                it.key.power = calculatedPower
            }
        }
    }
}