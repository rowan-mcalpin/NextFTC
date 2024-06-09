package com.rowanmcalpin.nextftc.driving

import com.rowanmcalpin.nextftc.hardware.MotorEx

@Suppress("PropertyName")
interface MecanumDriveConstants : DriveConstants {

    val LATERAL_MULTIPLIER: Double
    val DRIFT_MULTIPLIER: Double
    val DRIFT_TURN_MULTIPLIER: Double
    val BACKWARD_DRIFT_MULTIPLIER: Double
    val RIGHT_DRIFT_MULTIPLIER: Double
    val LEFT_FRONT_MOTOR: MotorEx
    val RIGHT_FRONT_MOTOR: MotorEx
    val LEFT_BACK_MOTOR: MotorEx
    val RIGHT_BACK_MOTOR: MotorEx
    val POV: DriverControlled.POV
    val REVERSE_STRAFE: Boolean
    val REVERSE_STRAIGHT: Boolean
    val REVERSE_TURN: Boolean

}