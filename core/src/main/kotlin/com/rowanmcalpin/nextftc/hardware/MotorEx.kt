package com.rowanmcalpin.nextftc.hardware

import com.qualcomm.robotcore.hardware.*
import com.rowanmcalpin.nextftc.Constants.opMode
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.PI

@Suppress("unused")
open class MotorEx (
    val name: () -> String,
    val type: MotorType = MotorType.ANDYMARK_NEVEREST,
    val ratio: Double = 1.0,
    val _direction: Direction
) {

    val ticksPerRev: Double
        get() = type.TICKS_PER_REVOLUTION * ratio

    open val motor: DcMotorEx
        get() {
            requireNotNull(_motor) { "Did not initialize the \"${name.invoke()}\" motor" }
            return _motor!!
        }
    private var _motor: DcMotorEx? = null

    constructor(name: String, type: MotorType, ratio: Double, direction: Direction) :
            this({ name }, type, ratio, direction)

    open fun initialize() {
        _motor = opMode.hardwareMap.get(DcMotorEx::class.java, name.invoke())
        _motor!!.direction = _direction
    }

    fun ticksToInches(ticks: Int, diameter: Double): Double =
        (ticks / ratio / type.TICKS_PER_REVOLUTION) * diameter * PI

    fun inchesToTicks(inches: Double, diameter: Double): Int =
        (inches * ratio * type.TICKS_PER_REVOLUTION / diameter / PI).toInt()

    // DcMotorEx Methods
    
    open fun setMotorEnable() = motor.setMotorEnable()
    open fun setMotorDisable() = motor.setMotorDisable()
    open val isMotorEnabled
        get() = motor.isMotorEnabled
    open fun setVelocity(angularRate: Double, unit: AngleUnit) = motor.setVelocity(angularRate, unit)
    open var velocity
        get() = motor.velocity
        set(it) {
            motor.velocity = it
        }
    open fun getVelocity(unit: AngleUnit) = motor.getVelocity(unit)
    @Deprecated("Replace with setPIDFCoefficients",
        replaceWith = ReplaceWith("setPIDFCoefficients"))
    open fun setPIDCoefficients(runMode: RunMode, pidCoefficients: PIDCoefficients) =
        @Suppress("DEPRECATION")
        motor.setPIDCoefficients(runMode, pidCoefficients)
    open fun setPIDFCoefficients(runMode: RunMode, pidfCoefficients: PIDFCoefficients) =
        motor.setPIDFCoefficients(runMode, pidfCoefficients)
    open fun setVelocityPIDFCoefficients(p: Double, i: Double, d: Double, f: Double) =
        motor.setVelocityPIDFCoefficients(p, i, d, f)
    open fun setPositionPIDFCoefficients(p: Double) = motor.setPositionPIDFCoefficients(p)
    open fun getPIDFCoefficients(runMode: RunMode): PIDFCoefficients? = motor.getPIDFCoefficients(runMode)
    open fun setTargetPositionTolerance(tolerance: Int) {
        motor.targetPositionTolerance = tolerance
    }
    open fun getTargetPositionTolerance() = motor.targetPositionTolerance
    open fun getCurrent(unit: CurrentUnit) = motor.getCurrent(unit)
    open fun getCurrentAlert(unit: CurrentUnit) = motor.getCurrentAlert(unit)
    open fun setCurrentAlert(current: Double, unit: CurrentUnit) = motor.setCurrentAlert(current, unit)
    open val isOverCurrent
        get() = motor.isOverCurrent

    // DcMotor Methods
    open var motorType: MotorConfigurationType
        get() = motor.motorType
        set(it) {
            motor.motorType = it
        }
    open val controller: DcMotorController?
        get() = motor.controller
    open val portNumber
        get() = motor.portNumber
    open var zeroPowerBehavior: ZeroPowerBehavior?
        get() = motor.zeroPowerBehavior
        set(it) {
            motor.zeroPowerBehavior = it
        }
    open val powerFloat
        get() = motor.powerFloat
    open var targetPosition
        get() = motor.targetPosition
        set(it) {
            motor.targetPosition = it
        }
    open val isBusy
        get() = motor.isBusy
    open val currentPosition
        get() = motor.currentPosition
    open var mode: RunMode?
        get() = motor.mode
        set(it) {
            motor.mode = it
        }

    // DcMotorSimple methods
    open var direction: Direction?
        get() = motor.direction
        set(it) {
            motor.direction = it
        }
    open var power
        get() = motor.power
        set(it) {
            motor.power = it
        }

    enum class MotorType(
        val TICKS_PER_REVOLUTION: Int,
        val RPM: Double,
        val STALL_TORQUE_OZIN: Double
    ) {
        TETRIX_MAX_TORQUENADO(24, 6000.0, 11.666667),
        ANDYMARK_NEVEREST(28, 6600.0, 8.75),
        MODERN_ROBOTICS_MATRIX(28, 6000.0, 20.45),
        GOBILDA_YELLOWJACKET(28, 6000.0, 20.45),
        REV_ROBOTICS_HD_HEX(28, 6000.0, 14.8693),
        REV_ROBOTICS_CORE_HEX(4, 9000.0, 6.2939),
    }
}
