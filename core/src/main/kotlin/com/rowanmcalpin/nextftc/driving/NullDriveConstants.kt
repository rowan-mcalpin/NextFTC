package com.rowanmcalpin.nextftc.driving

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.rowanmcalpin.nextftc.roadrunner.AxisDirection

/**
 * This class is used internally in conjunction with [com.rowanmcalpin.nextftc.driving.drivers.NullDrive] to provide a
 * default class that does not require a robot to be configured with a drivetrain.
 */
class NullDriveConstants: DriveConstants {
    override val TICKS_PER_REV: Double
        get() = 0.0
    override val MAX_RPM: Double
        get() = 0.0
    override val MOTOR_VEL_PID: PIDFCoefficients
        get() = PIDFCoefficients(1.0, 1.0, 1.0, 1.0)
    override val IS_RUN_USING_ENCODER: Boolean
        get() = false
    override val kV: Double
        get() = 0.0
    override val kA: Double
        get() = 0.0
    override val kStatic: Double
        get() = 0.0
    override val WHEEL_RADIUS: Double
        get() = 0.0
    override val GEAR_RATIO: Double
        get() = 0.0
    override val TRACK_WIDTH: Double
        get() = 1.0
    override val MAX_VEL: Double
        get() = 1.0
    override val MAX_ACCEL: Double
        get() = 1.0
    override val MAX_ANG_VEL: Double
        get() = 1.0
    override val MAX_ANG_ACCEL: Double
        get() = 1.0
    override val TRANSLATIONAL_PID: PIDCoefficients
        get() = PIDCoefficients(1.0, 1.0, 1.0)
    override val HEADING_PID: PIDCoefficients
        get() = PIDCoefficients(1.0, 1.0, 1.0)
    override val DRIVER_SPEEDS: List<Double>
        get() = listOf(0.0)
    override val VERTICAL_AXIS: AxisDirection
        get() = AxisDirection.POS_Z
}