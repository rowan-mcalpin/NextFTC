package com.rowanmcalpin.nextftc.driving.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.rowanmcalpin.nextftc.Constants
import com.rowanmcalpin.nextftc.Constants.drive
import com.rowanmcalpin.nextftc.driving.MecanumDriveConstants
import com.rowanmcalpin.nextftc.roadrunner.Encoder


/**
 * Determines position relative to previous position based on odometry wheels. Odometry wheels are
 * wheels that aren't attached to motors but are attached to encoders that can determine how much
 * the wheels have rotated. Based on how much they've rotated, we can determine the robot's
 * position. The reason why there are four constants assigned outside of the class is because we
 * need them to be outside of the class to use them in the constructor.
 */
class TwoWheelOdometryLocalizer(val constants: TwoWheelOdometryConstants) : TwoTrackingWheelLocalizer(listOf(
    Pose2d(constants.PARALLEL_X, constants.PARALLEL_Y, 0.0),
    Pose2d(constants.PERPENDICULAR_X, constants.PERPENDICULAR_Y, Math.toRadians(90.0))
)), SubsystemLocalizer {

    lateinit var perpendicularEncoder: Encoder
    lateinit var parallelEncoder: Encoder

    var lastWheelPosition = arrayListOf(0.0, 0.0)
    var wheelPositionChange = arrayListOf(0.0, 0.0)

    /**
     * Initializes the encoders & sets their direction
     */
    override fun initialize() {
        perpendicularEncoder = Encoder(Constants.opMode.hardwareMap.get(DcMotorEx::class.java, constants.PERPENDICULAR_NAME))
        parallelEncoder = Encoder(Constants.opMode.hardwareMap.get(DcMotorEx::class.java, constants.PARALLEL_NAME))

        if (constants.PERPENDICULAR_REVERSED) perpendicularEncoder.direction = Encoder.Direction.REVERSE
        if (constants.PARALLEL_REVERSED) parallelEncoder.direction = Encoder.Direction.REVERSE

    }

    /**
     * Returns the drive heading since this localizer only uses two wheels
     * @return the heading in radians
     */
    override fun getHeading(): Double {
        return drive.rawExternalHeading
    }

    /**
     * Returns the drive heading velocity since this localizer only uses two wheels
     * @return the radian/sec heading velocity
     */
    override fun getHeadingVelocity(): Double {
        return drive.externalHeadingVelocity
    }

    /**
     * Returns how many inches each wheel has turned, multiplied by X_MULTIPLIER & Y_MULTIPLIER
     * @return list of how much each wheel has turned in inches
     */
    override fun getWheelPositions(): List<Double> {
        wheelPositionChange[0] += if(parallelEncoder.currentPosition.toDouble() - lastWheelPosition[0] < 0)
            ((drive.constants as MecanumDriveConstants).BACKWARD_DRIFT_MULTIPLIER - 1.0) *
                    (parallelEncoder.currentPosition.toDouble() - lastWheelPosition[0]) else 0.0
        wheelPositionChange[1] += if(perpendicularEncoder.currentPosition.toDouble() - lastWheelPosition[1] < 0)
            ((drive.constants as MecanumDriveConstants).RIGHT_DRIFT_MULTIPLIER - 1.0) *
                    (perpendicularEncoder.currentPosition.toDouble() - lastWheelPosition[1]) else 0.0
        //TelemetryController.telemetry.addData("Current Position", parallelEncoder.currentPosition)
        //TelemetryController.telemetry.addData("Last Position", lastWheelPosition)
        //TelemetryController.telemetry.addData("Wheel Position Change", wheelPositionChange)
        val list = listOf(
            encoderTicksToInches(parallelEncoder.currentPosition.toDouble() + wheelPositionChange[0]) * constants.X_MULTIPLIER,
            encoderTicksToInches(perpendicularEncoder.currentPosition.toDouble() + wheelPositionChange[1]) * constants.Y_MULTIPLIER
        )
        lastWheelPosition[0] = parallelEncoder.currentPosition.toDouble()
        lastWheelPosition[1] = perpendicularEncoder.currentPosition.toDouble()
        return list
    }

    /**
     * Returns the inch/sec velocity of each wheel, multiplied by X_MULTIPLIER & Y_MULTIPLIER
     * @return list of inch/sec wheel velocities
     */
    override fun getWheelVelocities(): List<Double> {
        // If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method
        return if (constants.CORRECTED_VELOCITY) {
            listOf(
                encoderTicksToInches(perpendicularEncoder.correctedVelocity) * constants.Y_MULTIPLIER,
                if(encoderTicksToInches(parallelEncoder.correctedVelocity) * constants.X_MULTIPLIER < 0) encoderTicksToInches(parallelEncoder.correctedVelocity) * constants.X_MULTIPLIER * (drive.constants as MecanumDriveConstants).BACKWARD_DRIFT_MULTIPLIER else encoderTicksToInches(parallelEncoder.correctedVelocity) * constants.X_MULTIPLIER
            )
        } else {
            listOf(
                encoderTicksToInches(perpendicularEncoder.rawVelocity) * constants.Y_MULTIPLIER,
                if(encoderTicksToInches(parallelEncoder.rawVelocity) * constants.X_MULTIPLIER < 0) encoderTicksToInches(parallelEncoder.rawVelocity) * constants.X_MULTIPLIER * (drive.constants as MecanumDriveConstants).BACKWARD_DRIFT_MULTIPLIER else encoderTicksToInches(parallelEncoder.rawVelocity) * constants.X_MULTIPLIER
            )
        }
    }

    /**
     * Translates encoder ticks to inches. Remember that each pulse gets counted as four ticks.
     * @param ticks the encoder ticks to translate
     * @return the number of inches
     */
    private fun encoderTicksToInches(ticks: Double): Double {
        return constants.WHEEL_RADIUS * 2 * Math.PI * constants.GEAR_RATIO * ticks / constants.TICKS_PER_REV
    }
}

interface TwoWheelOdometryConstants {
    val PARALLEL_X: Double // in; forward offset of the parallel wheel
    val PARALLEL_Y: Double  // in; left offset of the parallel wheel
    val PERPENDICULAR_X: Double // in; forward offset of the perpendicular wheel
    val PERPENDICULAR_Y: Double // in; left offset of the perpendicular wheel
    val PARALLEL_NAME: String
    val PERPENDICULAR_NAME: String
    val TICKS_PER_REV: Double
    val WHEEL_RADIUS: Double
    val GEAR_RATIO: Double
    val PARALLEL_REVERSED: Boolean
    val PERPENDICULAR_REVERSED: Boolean
    val X_MULTIPLIER: Double
    val Y_MULTIPLIER: Double
    val CORRECTED_VELOCITY: Boolean
}