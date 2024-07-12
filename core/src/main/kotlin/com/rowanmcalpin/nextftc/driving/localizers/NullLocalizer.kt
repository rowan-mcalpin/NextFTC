package com.rowanmcalpin.nextftc.driving.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d

class NullLocalizer: SubsystemLocalizer {
    override var poseEstimate: Pose2d
        get() = Pose2d(0.0, 0.0, 0.0)
        set(value) { }

    override val poseVelocity: Pose2d?
        get() = Pose2d(0.0, 0.0, 0.0)

    override fun update() {
        // Nothing to update, as this localizer says the robot is always in one place.
    }
}