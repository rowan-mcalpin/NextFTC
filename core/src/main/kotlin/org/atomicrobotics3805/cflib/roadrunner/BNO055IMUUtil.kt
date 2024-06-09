package org.atomicrobotics3805.cflib.roadrunner

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.atomicrobotics3805.cflib.roadrunner.AxesSigns
import org.atomicrobotics3805.cflib.roadrunner.BNO055IMUUtil.InvalidAxisRemapException
import org.atomicrobotics3805.cflib.roadrunner.AxisDirection
import org.atomicrobotics3805.cflib.roadrunner.BNO055IMUUtil
import java.lang.RuntimeException

/**
 * Various utility functions for the BNO055 IMU.
 */
object BNO055IMUUtil {
    /**
     * Remap BNO055 IMU axes and signs. For reference, the default order is [AxesOrder.XYZ].
     * Call after [BNO055IMU.initialize]. Although this isn't
     * mentioned in the datasheet, the axes order appears to affect the onboard sensor fusion.
     *
     * Adapted from [this post](https://ftcforum.firstinspires.org/forum/ftc-technology/53812-mounting-the-revhub-vertically?p=56587#post56587).
     * Reference the [BNO055 Datasheet](https://www.bosch-sensortec.com/media/boschsensortec/downloads/datasheets/bst-bno055-ds000.pdf) for details.
     *
     * NOTE: Remapping axes can be somewhat confusing. Instead, use [.remapZAxis], if appropriate.
     *
     * @param imu IMU
     * @param order axes order
     * @param signs axes signs
     */
    fun swapThenFlipAxes(imu: BNO055IMU, order: AxesOrder, signs: AxesSigns) {
        try {
            // the indices correspond with the 2-bit axis encodings specified in the datasheet
            val indices = order.indices()
            // AxesSign's values align with the datasheet
            val axisMapSigns = signs.bVal
            if (indices[0] == indices[1] || indices[0] == indices[2] || indices[1] == indices[2]) {
                throw InvalidAxisRemapException("Same axis cannot be included in AxesOrder twice")
            }

            // ensure right-handed coordinate system
            val isXSwapped = indices[0] != 0
            val isYSwapped = indices[1] != 1
            val isZSwapped = indices[2] != 2
            val areTwoAxesSwapped = ((isXSwapped || isYSwapped || isZSwapped)
                    && (!isXSwapped || !isYSwapped || !isZSwapped))
            val oddNumOfFlips =
                axisMapSigns shr 2 xor (axisMapSigns shr 1) xor axisMapSigns and 1 == 1
            // != functions as xor
            if (areTwoAxesSwapped != oddNumOfFlips) {
                throw InvalidAxisRemapException("Coordinate system is left-handed")
            }

            // Bit:  7  6 |  5  4  |  3  2  |  1  0  |
            //   reserved | z axis | y axis | x axis |
            val axisMapConfig = indices[2] shl 4 or (indices[1] shl 2) or indices[0]

            // Enter CONFIG mode
            imu.write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.CONFIG.bVal.toInt() and 0x0F)
            Thread.sleep(100)

            // Write the AXIS_MAP_CONFIG register
            imu.write8(BNO055IMU.Register.AXIS_MAP_CONFIG, axisMapConfig and 0x3F)

            // Write the AXIS_MAP_SIGN register
            imu.write8(BNO055IMU.Register.AXIS_MAP_SIGN, axisMapSigns and 0x07)

            // Switch back to the previous mode
            imu.write8(BNO055IMU.Register.OPR_MODE, imu.parameters.mode.bVal.toInt() and 0x0F)
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    /**
     * Remaps the IMU coordinate system so that the remapped +Z faces the provided
     * [AxisDirection]. See [.swapThenFlipAxes] for details about the remapping.
     *
     * @param imu IMU
     * @param direction axis direction
     */
    fun remapZAxis(imu: BNO055IMU, direction: AxisDirection?) {
        when (direction) {
            AxisDirection.POS_X -> swapThenFlipAxes(imu, AxesOrder.ZYX, AxesSigns.NPP)
            AxisDirection.NEG_X -> swapThenFlipAxes(imu, AxesOrder.ZYX, AxesSigns.PPN)
            AxisDirection.POS_Y -> swapThenFlipAxes(imu, AxesOrder.XZY, AxesSigns.PNP)
            AxisDirection.NEG_Y -> swapThenFlipAxes(imu, AxesOrder.XZY, AxesSigns.PPN)
            AxisDirection.POS_Z -> swapThenFlipAxes(imu, AxesOrder.XYZ, AxesSigns.PPP)
            AxisDirection.NEG_Z -> swapThenFlipAxes(imu, AxesOrder.XYZ, AxesSigns.PNN)
            else -> {}
        }
    }

    /**
     * Now deprecated due to unintuitive parameter order.
     * Use [.swapThenFlipAxes] or [.remapZAxis] instead.
     *
     * @param imu IMU
     * @param order axes order
     * @param signs axes signs
     */
    @Deprecated("")
    fun remapAxes(imu: BNO055IMU, order: AxesOrder, signs: AxesSigns) {
        val adjustedAxesOrder = order.reverse()
        val indices = order.indices()
        val axisSignValue = signs.bVal xor (4 shr indices[0])
        val adjustedAxesSigns = AxesSigns.fromBinaryValue(axisSignValue)
        swapThenFlipAxes(imu, adjustedAxesOrder, adjustedAxesSigns)
    }

    /**
     * Error for attempting an illegal remapping (lhs or multiple same axes)
     */
    class InvalidAxisRemapException(detailMessage: String?) : RuntimeException(detailMessage)
}