package org.atomicrobotics3805.cflib.hardware

import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MotorExGroup(
    vararg val motors: MotorEx
): MotorEx(motors[0].name, motors[0].type, motors[0].ratio, motors[0]._direction) {

    override val motor: DcMotorEx
        get() = motors[0].motor
    override fun setMotorEnable() {
        motors.forEach {
            it.setMotorEnable()
        }
    }
    override fun setMotorDisable() {
        motors.forEach {
            it.setMotorDisable()
        }
    }
    override var velocity
        get() = motor.velocity
        set(value) {
            motors.forEach {
                it.velocity = value
            }
        }
    override fun getVelocity(unit: AngleUnit) = motors[0].getVelocity(unit)
    @Deprecated("Replace with setPIDFCoefficients",
        replaceWith = ReplaceWith("setPIDFCoefficients"))
    override fun setPIDCoefficients(runMode: DcMotor.RunMode, pidCoefficients: PIDCoefficients) {
        motors.forEach {
            @Suppress("DEPRECATION")
            it.setPIDCoefficients(runMode, pidCoefficients)
        }
    }
    override fun setPIDFCoefficients(runMode: DcMotor.RunMode, pidfCoefficients: PIDFCoefficients) {
        motors.forEach {
            it.setPIDFCoefficients(runMode, pidfCoefficients)
        }
    }
    override fun setVelocityPIDFCoefficients(p: Double, i: Double, d: Double, f: Double) {
        motors.forEach {
            it.setVelocityPIDFCoefficients(p, i, d, f)
        }
    }
    override fun setPositionPIDFCoefficients(p: Double) {
        motors.forEach {
            it.setPositionPIDFCoefficients(p)
        }
    }
    override fun getPIDFCoefficients(runMode: DcMotor.RunMode) = motors[0].getPIDFCoefficients(runMode)
    override fun setTargetPositionTolerance(tolerance: Int) {
        motors.forEach {
            it.setTargetPositionTolerance(tolerance)
        }
    }
    override fun getTargetPositionTolerance() = motors[0].getTargetPositionTolerance()
    override fun getCurrent(unit: CurrentUnit) = motors[0].getCurrent(unit)
    override fun getCurrentAlert(unit: CurrentUnit) = motors[0].getCurrentAlert(unit)
    override fun setCurrentAlert(current: Double, unit: CurrentUnit) {
        motors.forEach {
            it.setCurrentAlert(current, unit)
        }
    }
    override val isOverCurrent
        get() = motor.isOverCurrent

    // DcMotor Methods
    override var motorType: MotorConfigurationType
        get() = motor.motorType
        set(value) {
            motors.forEach {
                it.motorType = value
            }
        }
    override val controller: DcMotorController?
        get() = motor.controller
    override val portNumber
        get() = motor.portNumber
    override var zeroPowerBehavior: DcMotor.ZeroPowerBehavior?
        get() = motor.zeroPowerBehavior
        set(value) {
            motors.forEach {
                it.zeroPowerBehavior = value
            }
        }
    override val powerFloat
        get() = motor.powerFloat
    override var targetPosition
        get() = motor.targetPosition
        set(value) {
            motors.forEach {
                it.targetPosition = value
            }
        }
    override val isBusy
        get() = motor.isBusy
    override val currentPosition
        get() = motor.currentPosition
    override var mode: DcMotor.RunMode?
        get() = motor.mode
        set(value) {
            motors.forEach {
                it.mode = value
            }
        }

    // DcMotorSimple methods
    override var direction: DcMotorSimple.Direction?
        get() = motor.direction
        set(value) {
            motors.forEach {
                it.direction = value
            }
        }
    override var power
        get() = motor.power
        set(value) {
            motors.forEach {
                it.power = value
            }
        }


    override fun initialize() {
        for (motor in motors) {
            motor.initialize()
        }
    }
}