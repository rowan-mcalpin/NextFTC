package com.rowanmcalpin.nextftc.driving.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs

/**
 * This class is currently broken. We are working to resolve the issue.
 */
class PoseEstimator(
    private val odometryLocalizer: SubsystemLocalizer,
    private val vuforiaLocalizer: VuforiaLocalizer
) : SubsystemLocalizer {
    override var poseEstimate: Pose2d
        get() = odometryPositions.last().second + absoluteAdjustment + manualAdjustment
        set(value) {
            manualAdjustment = value - poseEstimate
        }
    override val poseVelocity: Pose2d?
        get() = odometryLocalizer.poseVelocity

    private val odometryPositions: ArrayList<Pair<Double, Pose2d>> = arrayListOf(Pair(0.0, Pose2d()))
    private var lastAbsolutePosition = Pose2d()
    private var absoluteAdjustment = Pose2d()
    private var manualAdjustment = Pose2d()

    private val timer = ElapsedTime()

    override fun initialize() {
        timer.reset()
    }

    override fun update() {
        odometryLocalizer.update()
        vuforiaLocalizer.update()
        odometryPositions.add(Pair(timer.milliseconds(), odometryLocalizer.poseEstimate))
        if (vuforiaLocalizer.poseEstimate != lastAbsolutePosition) {
            addAbsoluteMeasurement(lastAbsolutePosition, vuforiaLocalizer.constants.DELAY_TIME_MILLIS)
            lastAbsolutePosition = vuforiaLocalizer.poseEstimate
        }
    }

    private fun addAbsoluteMeasurement(position: Pose2d, delayTimeMillis: Double) {
        var nearestTime: Double? = null
        var nearestPair: Pair<Double, Pose2d>? = null
        for (pair in odometryPositions) {
            val time: Double = abs(timer.milliseconds() - delayTimeMillis - pair.first)
            if (nearestTime == null || time < nearestTime) {
                nearestPair = pair
                nearestTime = time
            }
        }
        if (nearestPair != null)
            absoluteAdjustment = position - nearestPair.second
    }
}