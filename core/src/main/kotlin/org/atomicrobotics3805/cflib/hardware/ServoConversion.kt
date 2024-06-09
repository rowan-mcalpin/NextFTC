package org.atomicrobotics3805.cflib.hardware

import com.qualcomm.robotcore.hardware.Servo

class ServoConversion(
    name: () -> String, type: ServoType? = null, speedOverride: Double? = null,
    val positionTranslator: (Double) -> Double,
    val directionTranslator: (Servo.Direction?) -> Servo.Direction? = { it },
    val scaleRangeTranslator: (Double, Double) -> Pair<Double, Double> = { min, max -> Pair(min, max)}
): ServoEx(name, type, speedOverride) {

    constructor(
        name: String, type: ServoType? = null, speedOverride: Double? = null,
        positionTranslator: (Double) -> Double,
        directionTranslator: (Servo.Direction?) -> Servo.Direction? = { it },
        scaleRangeTranslator: (Double, Double) -> Pair<Double, Double> = { min, max -> Pair(min, max)}):
            this({ name }, type, speedOverride, positionTranslator,
                directionTranslator, scaleRangeTranslator)
}