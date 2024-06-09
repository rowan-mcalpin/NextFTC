package org.atomicrobotics3805.cflib.hardware

import com.qualcomm.robotcore.hardware.Servo

class ServoExGroup(
    vararg val servos: ServoConversion
): ServoEx(servos[0].name, servos[0].type, servos[0].speedOverride) {


    override val servo = servos[0].servo

    override val manufacturer
        get() = servos[0].manufacturer
    override val deviceName
        get() = servos[0].deviceName
    override val connectionInfo
        get() = servos[0].connectionInfo
    override val version
        get() = servos[0].version
    override val controller
        get() = servos[0].controller
    override val portNumber
        get() = servos[0].portNumber
    override var direction
        get() = servos[0].direction
        set(value) {
            servos.forEach {
                it.direction = it.directionTranslator.invoke(value)
            }
        }
    override var position
        get() = servos[0].position
        set(value) {
            servos.forEach {
                it.position = it.positionTranslator.invoke(value)
            }
        }
    override fun scaleRange(min: Double, max: Double) {
        servos.forEach {
            val pair = it.scaleRangeTranslator.invoke(min, max)
            it.scaleRange(pair.first, pair.second)
        }
    }
    override fun resetDeviceConfigurationForOpMode() {
        servos.forEach {
            it.resetDeviceConfigurationForOpMode()
        }
    }
    override fun close() {
        servos.forEach {
            it.close()
        }
    }

    override fun initialize() {
        servos.forEach {
            it.initialize()
        }
    }
}