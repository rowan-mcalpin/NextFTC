package com.rowanmcalpin.nextftc.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.rowanmcalpin.nextftc.Constants.opMode
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoController

open class CRServoEx(
    val name: () -> String,
    val direction: Direction = Direction.FORWARD
) {

    open val servo: Servo
        get() {
            requireNotNull(_servo) { "Did not initialize the \"${name.invoke()}\" servo" }
            return _servo!!
        }
    private var _servo: Servo? = null

    constructor(name: String, direction: Direction = Direction.FORWARD) : this(
        { name },
        direction
    )

    open fun initialize() {
        _servo = opMode.hardwareMap.get(Servo::class.java, name.invoke())
    }
    
    // Servo Methods
    open val manufacturer: HardwareDevice.Manufacturer?
        get() = servo.manufacturer
    open val deviceName: String?
        get() = servo.deviceName
    open val connectionInfo: String?
        get() = servo.connectionInfo
    open val version
        get() = servo.version
    open fun resetDeviceConfigurationForOpMode() = servo.resetDeviceConfigurationForOpMode()
    open fun close() = servo.close()
    open val controller: ServoController?
        get() = servo.controller
    open val portNumber
        get() = servo.portNumber
    open var position
        get() = servo.position
        set(it) {
            servo.position = it
        }
    open fun scaleRange(min: Double, max: Double) = servo.scaleRange(min, max)

    enum class Direction {
        FORWARD,
        REVERSE
    }
}
