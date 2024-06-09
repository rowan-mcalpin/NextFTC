package com.rowanmcalpin.nextftc.roadrunner

import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.File
import java.util.*

/**
 * Utility functions for log files.
 */
object LoggingUtil {
    val ROAD_RUNNER_FOLDER = File(AppUtil.ROOT_FOLDER.toString() + "/RoadRunner/")
    private const val LOG_QUOTA = (25 * 1024 * 1024 // 25MB log quota for now
            ).toLong()

    private fun buildLogList(logFiles: MutableList<File>, dir: File) {
        for (file in dir.listFiles()) {
            if (file.isDirectory) {
                buildLogList(logFiles, file)
            } else {
                logFiles.add(file)
            }
        }
    }

    private fun pruneLogsIfNecessary() {
        val logFiles: MutableList<File> = ArrayList()
        buildLogList(logFiles, ROAD_RUNNER_FOLDER)
        Collections.sort(logFiles) { lhs: File, rhs: File ->
            java.lang.Long.compare(
                lhs.lastModified(),
                rhs.lastModified()
            )
        }
        var dirSize: Long = 0
        for (file in logFiles) {
            dirSize += file.length()
        }
        while (dirSize > LOG_QUOTA) {
            if (logFiles.size == 0) break
            val fileToRemove = logFiles.removeAt(0)
            dirSize -= fileToRemove.length()
            fileToRemove.delete()
        }
    }

    /**
     * Obtain a log file with the provided name
     */
    fun getLogFile(name: String?): File {
        ROAD_RUNNER_FOLDER.mkdirs()
        pruneLogsIfNecessary()
        return File(ROAD_RUNNER_FOLDER, name)
    }
}