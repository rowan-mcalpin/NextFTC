package com.rowanmcalpin.nextftc.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.rowanmcalpin.nextftc.command.Command
import com.qualcomm.robotcore.util.RobotLog
import com.rowanmcalpin.nextftc.hardware.MotorEx

/**
 * This command powers a motor and sets it to a certain mode if one is supplied.
 *
 * @param motor the motor to power
 * @param power the power (-1.0 to 1.0) to set it to
 * @param mode the RunMode that the motor should be set to, if any
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 */
class PowerMotor(
    private val motor: MotorEx,
    private val power: Double,
    private val mode: DcMotor.RunMode? = null,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true,
    private val logData: Boolean = false
) : Command() {

    override fun onStart() {
        if (mode != null) {
            motor.mode = mode
        }
        motor.power = power
        if(logData) {
            RobotLog.i("PowerMotor", power)
        }
    }
}