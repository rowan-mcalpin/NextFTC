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
package com.rowanmcalpin.nextftc.roadrunner

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.internal.system.Misc
import java.lang.NumberFormatException
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.HashMap

/**
 * Collection of utilites for interacting with Lynx modules.
 */
object LynxModuleUtil {
    private val MIN_VERSION = LynxFirmwareVersion(1, 8, 2)

    /**
     * Retrieve and parse Lynx module firmware version.
     * @param module Lynx module
     * @return parsed firmware version
     */
    fun getFirmwareVersion(module: LynxModule): LynxFirmwareVersion? {
        val versionString = module.nullableFirmwareVersionString ?: return null
        val parts = versionString.split(Regex("[ :,]+")).toTypedArray()
        return try {
            // note: for now, we ignore the hardware entry
            LynxFirmwareVersion(parts[3].toInt(), parts[5].toInt(), parts[7].toInt())
        } catch (e: NumberFormatException) {
            null
        }
    }

    /**
     * Ensure all of the Lynx modules attached to the robot satisfy the minimum requirement.
     * @param hardwareMap hardware map containing Lynx modules
     */
    fun ensureMinimumFirmwareVersion(hardwareMap: HardwareMap) {
        val outdatedModules: MutableMap<String, LynxFirmwareVersion?> = HashMap()
        for (module in hardwareMap.getAll(LynxModule::class.java)) {
            val version = getFirmwareVersion(module)
            if (version == null || version.compareTo(MIN_VERSION) < 0) {
                for (name in hardwareMap.getNamesOf(module)) {
                    outdatedModules[name] = version
                }
            }
        }
        if (outdatedModules.size > 0) {
            val msgBuilder = StringBuilder()
            msgBuilder.append("One or more of the attached Lynx modules has outdated firmware\n")
            msgBuilder.append(
                Misc.formatInvariant(
                    "Mandatory minimum firmware version: %s\n",
                    MIN_VERSION.toString()
                )
            )
            for ((key, value) in outdatedModules) {
                msgBuilder.append(
                    Misc.formatInvariant(
                        "\t%s: %s\n", key,
                        value?.toString() ?: "Unknown"
                    )
                )
            }
            throw LynxFirmwareVersionException(msgBuilder.toString())
        }
    }

    /**
     * Parsed representation of a Lynx module firmware version.
     */
    class LynxFirmwareVersion(val major: Int, val minor: Int, val eng: Int) :
        Comparable<LynxFirmwareVersion> {
        override fun equals(other: Any?): Boolean {
            return if (other is LynxFirmwareVersion) {
                val otherVersion = other
                major == otherVersion.major && minor == otherVersion.minor && eng == otherVersion.eng
            } else {
                false
            }
        }

        override fun compareTo(other: LynxFirmwareVersion): Int {
            val majorComp = Integer.compare(major, other.major)
            return if (majorComp == 0) {
                val minorComp = Integer.compare(minor, other.minor)
                if (minorComp == 0) {
                    Integer.compare(eng, other.eng)
                } else {
                    minorComp
                }
            } else {
                majorComp
            }
        }

        override fun toString(): String {
            return Misc.formatInvariant("%d.%d.%d", major, minor, eng)
        }
    }

    /**
     * Exception indicating an outdated Lynx firmware version.
     */
    class LynxFirmwareVersionException(detailMessage: String?) : RuntimeException(detailMessage)
}