package com.rowanmcalpin.nextftc.roadrunner

import com.acmerobotics.roadrunner.kinematics.Kinematics.calculateMotorFeedforward
import org.apache.commons.math3.stat.regression.SimpleRegression
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.ArrayList

/**
 * Various regression utilities.
 */
object RegressionUtil {
    /**
     * Numerically compute dy/dx from the given x and y values. The returned list is padded to match
     * the length of the original sequences.
     *
     * @param x x-values
     * @param y y-values
     * @return derivative values
     */
    private fun numericalDerivative(x: List<Double>, y: List<Double>): List<Double> {
        val deriv: MutableList<Double> = ArrayList(x.size)
        for (i in 1 until x.size - 1) {
            deriv.add(
                (y[i + 1] - y[i - 1]) /
                        (x[i + 1] - x[i - 1])
            )
        }
        // copy endpoints to pad output
        deriv.add(0, deriv[0])
        deriv.add(deriv[deriv.size - 1])
        return deriv
    }

    /**
     * Run regression to compute velocity and static feedforward from ramp test data.
     *
     * Here's the general procedure for gathering the requisite data:
     * 1. Slowly ramp the motor power/voltage and record encoder values along the way.
     * 2. Run a linear regression on the encoder velocity vs. motor power plot to obtain a slope
     * (kV) and an optional intercept (kStatic).
     *
     * @param timeSamples time samples
     * @param positionSamples position samples
     * @param powerSamples power samples
     * @param fitStatic fit kStatic
     * @param file log file
     */
    fun fitRampData(
        timeSamples: List<Double>, positionSamples: List<Double>,
        powerSamples: List<Double>, fitStatic: Boolean,
        file: File?
    ): RampResult {
        if (file != null) {
            try {
                PrintWriter(file).use { pw ->
                    pw.println("time,position,power")
                    for (i in timeSamples.indices) {
                        val time = timeSamples[i]
                        val pos = positionSamples[i]
                        val power = powerSamples[i]
                        pw.println("$time,$pos,$power")
                    }
                }
            } catch (e: FileNotFoundException) {
                // ignore
            }
        }
        val velSamples = numericalDerivative(timeSamples, positionSamples)
        val rampReg = SimpleRegression(fitStatic)
        for (i in timeSamples.indices) {
            val vel = velSamples[i]
            val power = powerSamples[i]
            rampReg.addData(vel, power)
        }
        return RampResult(
            Math.abs(rampReg.slope), Math.abs(rampReg.intercept),
            rampReg.rSquare
        )
    }

    /**
     * Run regression to compute acceleration feedforward.
     *
     * @param timeSamples time samples
     * @param positionSamples position samples
     * @param powerSamples power samples
     * @param rampResult ramp result
     * @param file log file
     */
    fun fitAccelData(
        timeSamples: List<Double>, positionSamples: List<Double>,
        powerSamples: List<Double>, rampResult: RampResult,
        file: File?
    ): AccelResult {
        if (file != null) {
            try {
                PrintWriter(file).use { pw ->
                    pw.println("time,position,power")
                    for (i in timeSamples.indices) {
                        val time = timeSamples[i]
                        val pos = positionSamples[i]
                        val power = powerSamples[i]
                        pw.println("$time,$pos,$power")
                    }
                }
            } catch (e: FileNotFoundException) {
                // ignore
            }
        }
        val velSamples = numericalDerivative(timeSamples, positionSamples)
        val accelSamples = numericalDerivative(timeSamples, velSamples)
        val accelReg = SimpleRegression(false)
        for (i in timeSamples.indices) {
            val vel = velSamples[i]
            val accel = accelSamples[i]
            val power = powerSamples[i]
            val powerFromVel = calculateMotorFeedforward(
                vel, 0.0, rampResult.kV, 0.0, rampResult.kStatic
            )
            val powerFromAccel = power - powerFromVel
            accelReg.addData(accel, powerFromAccel)
        }
        return AccelResult(Math.abs(accelReg.slope), accelReg.rSquare)
    }

    /**
     * Feedforward parameter estimates from the ramp regression and additional summary statistics
     */
    class RampResult(val kV: Double, val kStatic: Double, val rSquare: Double)

    /**
     * Feedforward parameter estimates from the ramp regression and additional summary statistics
     */
    class AccelResult(val kA: Double, val rSquare: Double)
}