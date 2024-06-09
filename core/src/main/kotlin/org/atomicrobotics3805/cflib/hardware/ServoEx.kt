package org.atomicrobotics3805.cflib.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice
import org.atomicrobotics3805.cflib.Constants.opMode
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoController

open class ServoEx(
    val name: () -> String,
    val type: ServoType? = null,
    val speedOverride: Double? = null
) {

    open val speed: Double
        get() {
            if (speedOverride != null) {
                return speedOverride
            }
            if (type != null) {
                return 60 * type.RANGE_REVOLUTIONS / type.RPM
            }
            return 1.0
        }
    open val servo: Servo
        get() {
            requireNotNull(_servo) { "Did not initialize the \"${name.invoke()}\" servo" }
            return _servo!!
        }
    private var _servo: Servo? = null

    constructor(name: String, type: ServoType? = null, speedOverride: Double? = null) : this(
        { name }, type, speedOverride
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
    open var direction: Servo.Direction?
        get() = servo.direction
        set(it) {
            servo.direction = it
        }
    open var position
        get() = servo.position
        set(it) {
            servo.position = it
        }
    open fun scaleRange(min: Double, max: Double) = servo.scaleRange(min, max)

    enum class ServoType(
        val RPM: Double,
        val RANGE_REVOLUTIONS: Double,
        val STALL_TORQUE_OZIN: Double
    ) {
        GOBILDA_TORQUE(40.0,0.83333, 240.0),
        GOBILDA_SUPER_SPEED(180.0, 0.83333, 55.0),
        GOBILDA_5_TURN_TORQUE(40.0, 5.0, 240.0),
        GOBILDA_SPEED(90.0, 0.83333, 110.0),
        HITEC_WINCH_SERVO(35.7,3.92, 152.75),
        REV_SMART_ROBOT_SERVO(76.9, 0.75, 187.8), // Could not find data for 4.8v, values are for powering at 6.0v
        HITEC_DELUXE(45.4545, 0.52777, 67.0)
    }
}
