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
package com.rowanmcalpin.nextftc

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import org.firstinspires.ftc.robotcore.external.Func
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.Telemetry.DisplayFormat
import org.firstinspires.ftc.robotcore.external.Telemetry.Log.DisplayOrder
import com.rowanmcalpin.nextftc.subsystems.Subsystem

/**
 * This object updates the telemetry each loop and smoothly combines the phone telemetry with
 * FtcDashboard telemetry. Normally, MultipleTelemetry limits the dashboard telemetry to whatever
 * the phone telemetry can do. With this object, however, you can access the dashboard packet and
 * do whatever you want with it. Some examples include drawing the robot, drawing the field, and
 * drawing the robot's position history.
 */
object TelemetryController : Subsystem {

    val packet: TelemetryPacket
        get() = dashboardAdapter.currentPacket!!
    lateinit var telemetry: MultipleTelemetry
    lateinit var dashboardAdapter: Adapter

    /**
     * Initializes the dashboardAdapter and telemetry instances
     */
    override fun initialize() {
        dashboardAdapter = Adapter(FtcDashboard.getInstance())
        telemetry = MultipleTelemetry(Constants.opMode.telemetry, dashboardAdapter)
    }

    /**
     * Updates the telemetry
     */
    override fun periodic() {
        telemetry.update()
    }
}

/**
 * This Adapter class is essentially just a Kotlin version of the FtcDashboard class with one major
 * difference: currentPacket is public. The purpose of the class is to let you use the FtcDashboard
 * in the same way that you would use phone telemetry: using addData(), update(), etc. The reason
 * why currentPacket has to be public is so that you can also do other stuff with it, such as
 * drawing the robot.
 * The functions here don't have KDoc comments because they're not necessary and I'm lazy.
 * @param dashboard use FtcDashboard.getInstance()
 */
class Adapter(private val dashboard: FtcDashboard) : Telemetry {
    var currentPacket: TelemetryPacket? = null
    private var log: LogAdapter? = null
    override fun addData(caption: String, format: String, vararg args: Any): Telemetry.Item {
        return addData(caption, String.format(format, *args))
    }

    override fun addData(caption: String, value: Any): Telemetry.Item? {
        currentPacket!!.put(caption, value)
        return null
    }

    override fun <T> addData(caption: String, valueProducer: Func<T>): Telemetry.Item {
        throw UnsupportedOperationException()
    }

    override fun <T> addData(
        caption: String,
        format: String,
        valueProducer: Func<T>
    ): Telemetry.Item {
        throw UnsupportedOperationException()
    }

    override fun removeItem(item: Telemetry.Item): Boolean {
        throw UnsupportedOperationException()
    }

    override fun clear() {
        currentPacket = TelemetryPacket()
        log = LogAdapter(currentPacket!!)
    }

    override fun clearAll() {
        currentPacket = TelemetryPacket()
    }

    override fun addAction(action: Runnable): Any {
        throw UnsupportedOperationException()
    }

    override fun removeAction(token: Any): Boolean {
        throw UnsupportedOperationException()
    }

    override fun speak(text: String) {
        throw UnsupportedOperationException()
    }

    override fun speak(text: String, languageCode: String, countryCode: String) {
        throw UnsupportedOperationException()
    }

    override fun update(): Boolean {
        dashboard.sendTelemetryPacket(currentPacket)
        clear()
        return true
    }

    override fun addLine(): Telemetry.Line? {
        return null
    }

    override fun addLine(lineCaption: String): Telemetry.Line? {
        currentPacket!!.addLine(lineCaption)
        return null
    }

    override fun removeLine(line: Telemetry.Line): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isAutoClear(): Boolean {
        return true
    }

    override fun setAutoClear(autoClear: Boolean) {}
    override fun getMsTransmissionInterval(): Int {
        return dashboard.telemetryTransmissionInterval
    }

    override fun setMsTransmissionInterval(msTransmissionInterval: Int) {
        dashboard.telemetryTransmissionInterval = msTransmissionInterval
    }

    override fun getItemSeparator(): String? {
        return null
    }

    override fun setItemSeparator(itemSeparator: String) {}
    override fun getCaptionValueSeparator(): String? {
        return null
    }

    override fun setCaptionValueSeparator(captionValueSeparator: String) {}
    override fun setDisplayFormat(displayFormat: DisplayFormat) {}
    override fun log(): Telemetry.Log {
        return log!!
    }

    init {
        clear()
    }

    private class LogAdapter(private val telemetryPacket: TelemetryPacket) :
        Telemetry.Log {
        override fun getCapacity(): Int {
            return 0
        }

        override fun setCapacity(capacity: Int) {}
        override fun getDisplayOrder(): DisplayOrder {
            return DisplayOrder.OLDEST_FIRST
        }

        override fun setDisplayOrder(displayOrder: DisplayOrder) {}
        override fun add(entry: String) {
            telemetryPacket.addLine(entry)
        }

        override fun add(format: String, vararg args: Any) {
            telemetryPacket.addLine(String.format(format, *args))
        }

        override fun clear() {
            telemetryPacket.clearLines()
        }
    }
}

// the reason this class is here is because I originally thought that we would have to recreate the
// MultipleTelemetry class from FtcDashboard with a public telemetryList, but I realized that we can
// access the variables in the telemetryList without the list itself
/*
class MultipleTelemetry(vararg telemetryList: Telemetry) {
    var telemetryList: MutableList<Telemetry>? = null
    private var log: MultipleLog? = null

    init {
        this.telemetryList = telemetryList.toMutableList()
        log = MultipleLog()
        for (telemetry in telemetryList) {
            log!!.addLog(telemetry.log())
        }
    }

    /**
     * Adds another telemetry object.
     * @param telemetry
     */
    fun addTelemetry(telemetry: Telemetry) {
        telemetryList!!.add(telemetry)
        log!!.addLog(telemetry.log())
    }

    fun addData(s: String?, s1: String?, vararg objects: Any?): Item? {
        val items: MutableList<Item> = ArrayList()
        for (telemetry in telemetryList!!) {
            items.add(telemetry.addData(s, s1, *objects))
        }
        return MultipleItem(items)
    }

    fun addData(s: String?, o: Any?): Item? {
        val items: MutableList<Item> = ArrayList()
        for (telemetry in telemetryList!!) {
            items.add(telemetry.addData(s, o))
        }
        return MultipleItem(items)
    }

    fun <T> addData(s: String?, func: Func<T>?): Item? {
        val items: MutableList<Item> = ArrayList()
        for (telemetry in telemetryList!!) {
            items.add(telemetry.addData(s, func))
        }
        return MultipleItem(items)
    }

    fun <T> addData(s: String?, s1: String?, func: Func<T>?): Item? {
        val items: MutableList<Item> = ArrayList()
        for (telemetry in telemetryList!!) {
            items.add(telemetry.addData(s, s1, func))
        }
        return MultipleItem(items)
    }

    fun removeItem(item: Item?): Boolean {
        var retVal = true
        for (telemetry in telemetryList!!) {
            val temp = telemetry.removeItem(item)
            retVal = retVal && temp
        }
        return retVal
    }

    fun clear() {
        for (telemetry in telemetryList!!) {
            telemetry.clear()
        }
    }

    fun clearAll() {
        for (telemetry in telemetryList!!) {
            telemetry.clearAll()
        }
    }

    fun addAction(runnable: Runnable?): Any? {
        for (telemetry in telemetryList!!) {
            telemetry.addAction(runnable)
        }
        // note: this behavior is correct given the current default Telemetry implementation
        return runnable
    }

    fun removeAction(o: Any?): Boolean {
        var retVal = true
        for (telemetry in telemetryList!!) {
            val temp = telemetry.removeAction(o)
            retVal = retVal && temp
        }
        return retVal
    }

    fun speak(text: String?) {
        for (telemetry in telemetryList!!) {
            telemetry.speak(text)
        }
    }

    fun speak(text: String?, languageCode: String?, countryCode: String?) {
        for (telemetry in telemetryList!!) {
            telemetry.speak(text, languageCode, countryCode)
        }
    }

    fun update(): Boolean {
        var retVal = true
        for (telemetry in telemetryList!!) {
            val temp = telemetry.update()
            retVal = retVal && temp
        }
        return retVal
    }

    fun addLine(): Line? {
        val lines: MutableList<Line> = ArrayList()
        for (telemetry in telemetryList!!) {
            lines.add(telemetry.addLine())
        }
        return MultipleLine(lines)
    }

    fun addLine(s: String?): Line? {
        val lines: MutableList<Line> = ArrayList()
        for (telemetry in telemetryList!!) {
            lines.add(telemetry.addLine(s))
        }
        return MultipleLine(lines)
    }

    fun removeLine(line: Line?): Boolean {
        var retVal = true
        for (telemetry in telemetryList!!) {
            val temp = telemetry.removeLine(line)
            retVal = retVal && temp
        }
        return retVal
    }

    fun isAutoClear(): Boolean {
        return telemetryList!![0].isAutoClear
    }

    fun setAutoClear(b: Boolean) {
        for (telemetry in telemetryList!!) {
            telemetry.isAutoClear = b
        }
    }

    fun getMsTransmissionInterval(): Int {
        return telemetryList!![0].msTransmissionInterval
    }

    fun setMsTransmissionInterval(i: Int) {
        for (telemetry in telemetryList!!) {
            telemetry.msTransmissionInterval = i
        }
    }

    fun getItemSeparator(): String? {
        return telemetryList!![0].itemSeparator
    }

    fun setItemSeparator(s: String?) {
        for (telemetry in telemetryList!!) {
            telemetry.itemSeparator = s
        }
    }

    fun getCaptionValueSeparator(): String? {
        return telemetryList!![0].captionValueSeparator
    }

    fun setCaptionValueSeparator(s: String?) {
        for (telemetry in telemetryList!!) {
            telemetry.captionValueSeparator = s
        }
    }

    fun setDisplayFormat(displayFormat: DisplayFormat?) {
        for (telemetry in telemetryList!!) {
            telemetry.setDisplayFormat(displayFormat)
        }
    }

    fun log(): Log? {
        return log
    }

    class MultipleItem(private val items: List<Item>) : Item {

        override fun getCaption(): String = items[0].caption
        override fun setCaption(s: String?): Item {
            for (item in items) {
                item.caption = s
            }
            return this
        }

        override fun setValue(s: String?, vararg objects: Any?): Item {
            for (item in items) {
                item.setValue(s, objects)
            }
            return this
        }

        override fun setValue(o: Any?): Item {
            for (item in items) {
                item.setValue(o)
            }
            return this
        }

        override fun <T> setValue(func: Func<T>?): Item {
            for (item in items) {
                item.setValue(func)
            }
            return this
        }

        override fun <T> setValue(s: String?, func: Func<T>?): Item {
            for (item in items) {
                item.setValue(s, func)
            }
            return this
        }

        override fun setRetained(aBoolean: Boolean?): Item {
            for (item in items) {
                item.setRetained(aBoolean)
            }
            return this
        }

        override fun isRetained(): Boolean = items[0].isRetained

        override fun addData(s: String?, s1: String?, vararg objects: Any?): Item {
            for (item in items) {
                item.addData(s, s1, objects)
            }
            return this
        }

        override fun addData(s: String?, o: Any?): Item {
            for (item in items) {
                item.addData(s, o)
            }
            return this
        }

        override fun <T> addData(s: String?, func: Func<T>?): Item {
            for (item in items) {
                item.addData(s, func)
            }
            return this
        }

        override fun <T> addData(s: String?, s1: String?, func: Func<T>?): Item {
            for (item in items) {
                item.addData(s, s1, func)
            }
            return this
        }
    }

    class MultipleLine(private val lines: List<Line>) : Line {

        override fun addData(s: String?, s1: String?, vararg objects: Any?): Item {
            val items: MutableList<Item> = ArrayList()
            for (line in lines) {
                items.add(line.addData(s, s1, objects))
            }
            return MultipleItem(items)
        }

        override fun addData(s: String?, o: Any?): Item {
            val items: MutableList<Item> = ArrayList()
            for (line in lines) {
                items.add(line.addData(s, o))
            }
            return MultipleItem(items)
        }

        override fun <T> addData(s: String?, func: Func<T>?): Item {
            val items: MutableList<Item> = ArrayList()
            for (line in lines) {
                items.add(line.addData(s, func))
            }
            return MultipleItem(items)
        }

        override fun <T> addData(s: String?, s1: String?, func: Func<T>?): Item {
            val items: MutableList<Item> = ArrayList()
            for (line in lines) {
                items.add(line.addData(s, s1, func))
            }
            return MultipleItem(items)
        }

    }

    class MultipleLog : Log {
        private val logs: MutableList<Log>
        fun addLog(log: Log) {
            logs.add(log)
        }

        override fun getCapacity(): Int = logs[0].capacity
        override fun setCapacity(capacity: Int) {
                for (log in logs) {
                    log.capacity = capacity
                }
            }
        override fun getDisplayOrder(): DisplayOrder? = logs[0].displayOrder
        override fun setDisplayOrder(displayOrder: DisplayOrder?) {
                for (log in logs) {
                    log.displayOrder = displayOrder
                }
            }

        override fun add(s: String?) {
            for (log in logs) {
                log.add(s)
            }
        }

        override fun add(s: String?, vararg objects: Any?) {
            for (log in logs) {
                log.add(s, objects)
            }
        }

        override fun clear() {
            for (log in logs) {
                log.clear()
            }
        }

        init {
            logs = ArrayList()
        }
    }
}*/