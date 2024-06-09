package com.rowanmcalpin.nextftc.driving

import com.rowanmcalpin.nextftc.hardware.MotorEx

@Suppress("PropertyName")
interface TankDriveConstants : DriveConstants {

    val LEFT_MOTORS: MotorEx
    val RIGHT_MOTORS: MotorEx
}