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

package com.rowanmcalpin.nextftc.ftc.tuning

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.GamepadNotConnectedException
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * An automatic tuner for linear slide feedforward.
 * @param motors The motors to tune
 * @param encoderIndex The index of the motor to use the encoder of
 * @param telemetry The telemetry instance to use
 */
class LinearSlideFeedforwardTuner(private val motors: List<DcMotorEx>, private val encoderIndex: Int, private val telemetry: Telemetry): Command() {

    /**
     * @param motor The motor to tune
     * @param telemetry The telemetry instance to use
     */
    constructor(motor: DcMotorEx, telemetry: Telemetry): this(listOf(motor), 0, telemetry)

    public val FEEDFORWARD_INCREASE: Double = 0.001
    public val FEEDFORWARD_DECREASE: Double = -0.0002

    enum class Step {
        INIT,
        STEP_1,
        TEST_0,
        STEP_2,
        STEP_3,
        DONE,
    }

    private var currentEncoderValue: Int = 0
    private var previousEncoderValue: Int = 0
    private var rememberedEncoderValue: Int = 0
    private var currentMotorPower: Double = 0.0

    private val timer: ElapsedTime = ElapsedTime()

    private var step = Step.INIT

    override val isDone: Boolean = false

    override fun start() {
        if (encoderIndex !in motors.indices) {
            throw IllegalArgumentException("Encoder index must be a valid index in the list of motors")
        }

        if (OpModeData.gamepad1 == null) {
            throw GamepadNotConnectedException(1)
        }

        motors[encoderIndex].mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        for (motor in motors) {
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        telemetry.addLine("NextFTC Linear Slide Feedforward Tuner")
        telemetry.addLine("Hold the slide halfway up and press start/options on gamepad 1 to begin.")
        telemetry.addLine("Then let go.")
        telemetry.addLine("WARNING: Make sure all motors are properly reversed.")
        telemetry.addLine("If they fight against each other, bad things will happen.")
    }

    override fun update() {
        currentEncoderValue = motors[encoderIndex].currentPosition
        telemetry.addData("Current Encoder Value", currentEncoderValue)
        telemetry.addData("Current Step", step)

        when (step) {
            Step.INIT -> {
                if (OpModeData.gamepad1!!.start) {
                    // Advance to step 1
                    step = Step.STEP_1
                }
            }

            Step.STEP_1 -> {
                if (currentEncoderValue == previousEncoderValue) {
                    // Ensure that 0 is really the correct feedforward
                    step = Step.TEST_0
                    timer.reset()
                    rememberedEncoderValue = currentEncoderValue
                }
                if (currentEncoderValue > previousEncoderValue) {
                    // Slide was increasing with no power, so encoder must be reversed.
                    throw IllegalArgumentException("Make sure to reverse the motor direction before calling this command!")
                }
                if (currentEncoderValue < previousEncoderValue) {
                    // Advance to step 2
                    step = Step.STEP_2
                }
            }

            Step.TEST_0 -> {
                // Time is in seconds because seconds are the default resolution of ElapsedTime
                if (timer.time() > 0.5) {
                    if (currentEncoderValue == rememberedEncoderValue) {
                        step = Step.DONE
                    } else if (currentEncoderValue < rememberedEncoderValue) {
                        step = Step.STEP_2
                    } else {
                        throw IllegalArgumentException("Make sure to reverse the motor direction before calling this command!")
                    }
                }
            }
            Step.STEP_2 -> {
                currentMotorPower += FEEDFORWARD_INCREASE
                if (currentEncoderValue > previousEncoderValue) {
                    // Advance to step 3
                    step = Step.STEP_3
                } else if (currentEncoderValue == previousEncoderValue) {
                    step = Step.DONE
                }
            }
            Step.STEP_3 -> {
                currentMotorPower += FEEDFORWARD_DECREASE
                if (currentEncoderValue <= previousEncoderValue) {
                    step = Step.DONE
                }
            }
            Step.DONE -> {
                telemetry.addLine("Feedforward: $currentMotorPower")
            }
        }

        previousEncoderValue = currentEncoderValue
        for (motor in motors) {
            motor.power = currentMotorPower
        }
    }
}