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
package org.atomicrobotics3805.cflib.utilCommands

import com.qualcomm.robotcore.util.ElapsedTime
import org.atomicrobotics3805.cflib.Command
import org.atomicrobotics3805.cflib.TelemetryController

/**
 * This class is used to repeatedly send telemetry messages for a certain period of time.
 *
 * @param time the time in seconds to send the message for
 * @param message a lambda that returns a String to use as the message. For example,
 *                { motor.currentPosition.toString() }
 */
@SuppressWarnings("unused")
class TelemetryCommand(private val time: Double, private val message: () -> String) : Command() {

    val timer = ElapsedTime()
    override val _isDone: Boolean
        get() = timer.seconds() > time

    /**
     * Uses a caption and a data parameter instead of just a message. Like telemetry.addData().
     * @param time the time in seconds to send the message for
     * @param caption a caption for the data
     * @param data the actual data
     */
    constructor(time: Double, caption: String, data: () -> String) :
            this(time, { caption + ": " + data.invoke() })
    /**
     * Uses a String as a message instead of a lambda. Should not be used if the message is being
     * updated.
     * @param time the time in seconds to send the message for
     * @param message the message to use in the telemetry statement
     */
    constructor(time: Double, message: String) :
            this(time, { message })
    /**
     * Also uses a caption and a data parameter instead of just a message, but this time uses a
     * String for data. Should not be used if data is being updated.
     * @param time the time in seconds to send the message for
     * @param caption a caption for the data
     * @param data the actual data
     */
    constructor(time: Double, caption: String, data: String) :
            this(time, { "$caption: $data" })

    /**
     * Resets timer
     */
    override fun start() {
        timer.reset()
    }

    /**
     * Adds telemetry line
     */
    override fun execute() {
        TelemetryController.telemetry.addLine(message.invoke())
    }
}