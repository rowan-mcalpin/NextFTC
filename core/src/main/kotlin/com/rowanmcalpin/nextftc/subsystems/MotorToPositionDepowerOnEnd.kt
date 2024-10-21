package com.rowanmcalpin.nextftc.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.command.CommandScheduler
import com.rowanmcalpin.nextftc.command.utility.TelemetryCommand
import com.qualcomm.robotcore.util.RobotLog
import com.rowanmcalpin.nextftc.hardware.MotorEx
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * This class rotates a motor to a certain position. It proportionally slows down as it nears that
 * position. It starts slowing down 1 / kP ticks away from its target position.
 *
 * On end, it depowers the motor
 *
 * @param motor the motor to move
 * @param targetPosition where the motor should move to
 * @param speed how fast it should move there
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 * @param minError minimum error
 * @param kP multiplied by the error and speed to get the
 */
@Suppress("MemberVisibilityCanBePrivate")
open class MotorToPositionDepowerOnEnd(
    protected val motor: MotorEx,
    protected val targetPosition: Int,
    protected var speed: Double,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true,
    protected val minError: Int = 15,
    protected val kP: Double = 0.005,
    protected val logData: Boolean = false
) : Command() {

    protected val timer = ElapsedTime()
    protected val positions: MutableList<Int> = mutableListOf()
    protected val savesPerSecond = 10.0
    protected var saveTimes: MutableList<Double> = mutableListOf()
    protected val minimumChangeForStall = 20.0
    protected var error: Int = 0
    protected var direction: Double = 0.0
    override val _isDone: Boolean
        get() = abs(error) < minError



    /**
     * Sets the motor's mode to RUN_USING_ENCODER, sets the error to the difference between the target and current
     * positions, and sets the direction to the sign of the error
     */
    override fun onStart() {
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        error = targetPosition - motor.currentPosition
        direction = sign(error.toDouble())
    }

    /**
     * Updates the error and direction, then calculates and sets the motor power
     */
    override fun onExecute() {
        error = targetPosition - motor.currentPosition
        direction = sign(error.toDouble())
        val power = kP * abs(error) * speed * direction
        motor.power = Range.clip(power, -min(speed, 1.0), min(speed, 1.0))
        // cancelIfStalled()
        if(logData) {
            val data = "Power: " + Range.clip(power, -min(speed, 1.0), min(speed, 1.0)) + ", direction: " + direction + ", error: " + error
            RobotLog.i("MotorToPosition %s", data)
        }
    }

    /**
     * Stops the motor
     */
    override fun onEnd(interrupted: Boolean) {
        motor.power = speed
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        motor.targetPosition = motor.currentPosition
    }

    /**
     * Starts by determining whether a stall check has been performed in the past 1 / savesPerSecond seconds. If not,
     * It then compares the speed from the previous check to the current speed. If there's a change of at least
     * minimumChangeForStall times, then the motor is stalled. It sends out a telemetry message and cancels the command.
     */
    @Deprecated("This function may be contributing to a major issue with the command. Do not use.")
    fun cancelIfStalled() {
        val lastTime = if (saveTimes.size == 0) 0.0 else saveTimes.last()
        val roundedLastTime = (lastTime * savesPerSecond).roundToInt() / savesPerSecond
        if (timer.seconds() - roundedLastTime < 1 / savesPerSecond) {
            if (positions.size > 1) {
                val lastSpeed = abs(positions[positions.size - 2] - positions[positions.size - 1])
                val currentSpeed = abs(positions[positions.size - 1] - motor.currentPosition)
                if (currentSpeed == 0 || lastSpeed / currentSpeed >= minimumChangeForStall) {
                    CommandScheduler.scheduleCommand(
                        TelemetryCommand(3.0, "Motor " + motor.name + " Stalled!")
                    )
                    isDone = true
                }
            }
            saveTimes.add(timer.seconds())
            positions.add(motor.currentPosition)
        }

    }
}