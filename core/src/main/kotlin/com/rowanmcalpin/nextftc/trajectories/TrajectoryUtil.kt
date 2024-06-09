/*
    Copyright (c) 2022 Atomic Robotics (https://atomicrobotics3805.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.rowanmcalpin.nextftc.trajectories

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import com.rowanmcalpin.nextftc.Constants.Color.BLUE
import com.rowanmcalpin.nextftc.Constants.color

val Double.inchesToMm get() = this * 25.4
val Double.mmToInches get() = this / 25.4
val Double.toRadians get() = (Math.toRadians(this))
val Double.rad get() = (this.toRadians)
val Double.switch get() = (this.switchColor)
val Double.switchAngle get() = (this.switchColorAngle)
val Double.switchColorAngle get () = (if (color == BLUE) this else 360 - this)
val Double.switchApproachTangentAngle get () = (if (color == BLUE) this else this - 180)
val Double.switchColor get () = (if (color == BLUE) this else this * -1)
val Double.flipAlongX36 get() = (if (color == BLUE) this else 72 - this)
val Double.translateAcrossField get() = (if (color == BLUE) this else (72 - this) * -1)

/** Returns the Vector2d of a Pose2d **/
val Pose2d.v : Vector2d get() = (this.vec())
/** Returns the heading of a Pose2d (in radians) **/
val Pose2d.h : Double get() = (this.heading)

fun Pose2d(matrix: OpenGLMatrix) = Pose2d(
    matrix.translation.get(0).toDouble().mmToInches,
    // TODO("Figure out where this is matrix.get(1) (y) or matrix.get(2) (z)")
    matrix.translation.get(1).toDouble().mmToInches,
    Orientation.getOrientation(matrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES)
        .thirdAngle.toDouble().toRadians
)
