package com.rowanmcalpin.nextftc.subsystems

import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.path.Path
import com.rowanmcalpin.nextftc.Constants.drive
import com.rowanmcalpin.nextftc.TelemetryController
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.roadrunner.DashboardUtil
import kotlin.math.cos
import kotlin.math.sin


/**
 * Untested
 */
class DisplayRobot(
    val width: Double = 18.0, val length: Double = 18.0,
    val headingLineLength: Double = 80.0, val poseHistoryLimit: Int = 100
) : Command() {

    override val _isDone = false
    override val interruptible = false

    val pathHistory = mutableListOf<Path>()
    val actualPoseHistory = mutableListOf<Pose2d>()
    val targetPoseHistory = mutableListOf<Pose2d>()

    override fun onExecute() {
        // saves the robot's actual and target position history
        if (actualPoseHistory.size < 1 || drive.poseEstimate != actualPoseHistory.last()) {
            actualPoseHistory.add(drive.poseEstimate)
            if (drive.trajectory != null) {
                targetPoseHistory.add(drive.trajectory!!.trajectory[drive.follower.elapsedTime()])
            }
        }
        // caps the size of the poseHistory lists
        if (poseHistoryLimit > -1 && actualPoseHistory.size > poseHistoryLimit) {
            actualPoseHistory.removeFirst()
        }
        if (poseHistoryLimit > -1 && targetPoseHistory.size > poseHistoryLimit) {
            targetPoseHistory.removeFirst()
        }
        // saves the robot's path history
        if (drive.trajectory != null &&
            (pathHistory.isEmpty() || !pathHistory.contains(drive.trajectory!!.trajectory.path))) {
            pathHistory.add(drive.trajectory!!.trajectory.path)
        }
        val fieldOverlay = TelemetryController.packet.fieldOverlay()
        TelemetryController.telemetry.addData("x", drive.poseEstimate.x)
        TelemetryController.telemetry.addData("y", drive.poseEstimate.y)
        TelemetryController.telemetry.addData("heading", drive.poseEstimate.heading)

        // draws the robot's paths
        fieldOverlay.setStroke("#D4232340") // red, quarter alpha
        for (path in pathHistory) {
            DashboardUtil.drawSampledPath(fieldOverlay, path)
        }

        // draws the target robot position and its target position history
        fieldOverlay.setStroke("#28D436") // green
        if (drive.trajectory != null) {
            drawRobot(fieldOverlay, drive.trajectory!!.trajectory[drive.follower.elapsedTime()])
        }
        DashboardUtil.drawPoseHistory(fieldOverlay, targetPoseHistory)

        // draws the robot and its actual position history
        fieldOverlay.setStroke("#3F51B5") // blue
        drawRobot(fieldOverlay, drive.poseEstimate)
        DashboardUtil.drawPoseHistory(fieldOverlay, actualPoseHistory)

        // draws a line pointing where the robot is looking
        fieldOverlay.setStroke("#db03fc40") // purple, quarter alpha
        drawHeadingLine(fieldOverlay, drive.poseEstimate)
    }

    fun drawHeadingLine(canvas: Canvas, pose: Pose2d) {
        val end = Vector2d(
            pose.x - cos(pose.heading) * headingLineLength,
            pose.y + sin(pose.heading) * headingLineLength
        )
        canvas.strokeLine(pose.x, pose.y, end.x, end.y)
    }

    fun drawRobot(canvas: Canvas, pose: Pose2d) {
        val corners = listOf(
            Vector2d(-length / 2, -width / 2),
            Vector2d(-length / 2, width / 2),
            Vector2d(length / 2, width / 2),
            Vector2d(length / 2, -width / 2)
        )
        val xPoints = DoubleArray(4)
        val yPoints = DoubleArray(4)
        for (i in 0..3) {
            xPoints[i] = corners[i].rotated(pose.heading).x + pose.x
            yPoints[i] = corners[i].rotated(pose.heading).y + pose.y
        }
        canvas.strokePolygon(xPoints, yPoints)
    }
}