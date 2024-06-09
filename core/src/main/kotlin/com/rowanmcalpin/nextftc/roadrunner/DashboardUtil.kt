package com.rowanmcalpin.nextftc.roadrunner

import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.roadrunner.geometry.Pose2d
import kotlin.jvm.JvmOverloads
import com.acmerobotics.roadrunner.path.Path

/**
 * Set of helper functions for drawing Road Runner paths and trajectories on dashboard canvases.
 */
object DashboardUtil {
    private const val DEFAULT_RESOLUTION = 2.0 // distance units; presumed inches
    private const val ROBOT_RADIUS = 9.0 // in
    fun drawPoseHistory(canvas: Canvas, poseHistory: List<Pose2d>) {
        val xPoints = DoubleArray(poseHistory.size)
        val yPoints = DoubleArray(poseHistory.size)
        for (i in poseHistory.indices) {
            val (x, y) = poseHistory[i]
            xPoints[i] = x
            yPoints[i] = y
        }
        canvas.strokePolyline(xPoints, yPoints)
    }

    @JvmOverloads
    fun drawSampledPath(canvas: Canvas, path: Path, resolution: Double = DEFAULT_RESOLUTION) {
        val samples = Math.ceil(path.length() / resolution).toInt()
        val xPoints = DoubleArray(samples)
        val yPoints = DoubleArray(samples)
        val dx = path.length() / (samples - 1)
        for (i in 0 until samples) {
            val displacement = i * dx
            val (x, y) = path[displacement]
            xPoints[i] = x
            yPoints[i] = y
        }
        canvas.strokePolyline(xPoints, yPoints)
    }

    fun drawRobot(canvas: Canvas, pose: Pose2d) {
        canvas.strokeCircle(pose.x, pose.y, ROBOT_RADIUS)
        val (x, y) = pose.headingVec().times(ROBOT_RADIUS)
        val x1 = pose.x + x / 2
        val y1 = pose.y + y / 2
        val x2 = pose.x + x
        val y2 = pose.y + y
        canvas.strokeLine(x1, y1, x2, y2)
    }
}