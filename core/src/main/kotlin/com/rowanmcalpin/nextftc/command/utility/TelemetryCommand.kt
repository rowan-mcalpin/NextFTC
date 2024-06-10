package com.rowanmcalpin.nextftc.command.utility

import com.qualcomm.robotcore.util.ElapsedTime
import com.rowanmcalpin.nextftc.command.Command
import com.rowanmcalpin.nextftc.TelemetryController

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