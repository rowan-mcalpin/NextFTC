package com.rowanmcalpin.nextftc.driving

import com.acmerobotics.roadrunner.drive.DriveSignal
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator.generateSimpleMotionProfile
import com.acmerobotics.roadrunner.profile.MotionState
import com.qualcomm.robotcore.util.ElapsedTime
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.Constants.drive
import com.rowanmcalpin.nextftc.subsystems.Subsystem
import com.rowanmcalpin.nextftc.trajectories.toRadians

/**
 * This command tells the robot to turn to a certain angle in radians. Depending on turnType, the angle can be either
 * relative or absolute. The 0 angle is pointing away from the audience and left is positive.
 *
 * @param angle the angle in radians to turn to
 * @param MAX_VEL the maximum velocity in radians/second that the robot can turn at
 * @param MAX_ACCEL the maximum acceleration in radians/second^2 that the robot can turn at
 * @param turnType the type of turn, either relative or absolute
 * @param requirements any Subsystems used by this command
 * @param interruptible whether this command can be interrupted or not
 */
@Suppress("unused")
open class Turn(
    private var angle: Double,
    private val MAX_VEL: Double,
    private val MAX_ACCEL: Double,
    private val turnType: TurnType,
    override val requirements: List<Subsystem> = arrayListOf(),
    override val interruptible: Boolean = true
) : Command() {

    enum class TurnType {
        ABSOLUTE,
        RELATIVE
    }

    override val _isDone: Boolean
        get() = turnProfile.duration() < timer.seconds()

    private val timer = ElapsedTime()

    private lateinit var turnProfile: MotionProfile
    private val startPose = drive.poseEstimate

    /**
     * Makes sure the robot doesn't turn more than 180 degrees, then sets up the turnProfile and resets the timer
     */
    override fun onStart() {
        if (turnType == TurnType.ABSOLUTE) {
            angle -= drive.poseEstimate.heading
        }
        if (angle > 180.0.toRadians)
            angle -= 360.0.toRadians
        if (angle < (-180.0).toRadians)
            angle += 360.0.toRadians
        turnProfile = generateSimpleMotionProfile(
            MotionState(drive.poseEstimate.heading, 0.0, 0.0, 0.0),
            MotionState(drive.poseEstimate.heading + angle, 0.0, 0.0, 0.0),
            MAX_VEL,
            MAX_ACCEL
        )
        timer.reset()
    }

    /**
     * Calculates and sets the new drive signal
     */
    override fun onExecute() {
        val t = timer.seconds()
        val targetState = turnProfile[t]
        drive.turnController.targetPosition = targetState.x
        val correction = drive.turnController.update(drive.poseEstimate.heading)
        val targetOmega = targetState.v
        val targetAlpha = targetState.a
        drive.setDriveSignal(
            DriveSignal(
                Pose2d(0.0, 0.0, targetOmega + correction),
                Pose2d(0.0, 0.0, targetAlpha)
            )
        )
    }

    /**
     * Makes sure the robot understands its heading and stops it
     */
    override fun onEnd(interrupted: Boolean) {
        drive.poseEstimate = Pose2d(drive.poseEstimate.vec(), drive.rawExternalHeading)
        drive.setDriveSignal(DriveSignal())
    }

}