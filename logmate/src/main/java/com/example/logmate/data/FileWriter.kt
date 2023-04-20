package com.example.logmate.data

import android.content.Context
import android.util.Log
import com.example.logmate.util.Common
import com.example.logmate.util.LogPriority
import com.squareup.tape2.QueueFile
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/*
 * Created by Sudhanshu Kumar on 19/04/23.
 */

private const val LOGS_FOLDER_NAME = "logs"

class FileWriter(
    private val context: Context,
    private val maxFileSize: Int
) {

    private var areObjectsInitialized = false
    private val bufferLogList = mutableListOf<String>()
    private var hasDelayedLogsAdded = false

    private var coroutineContext: CoroutineContext = SupervisorJob()
    private val serviceScope = CoroutineScope(coroutineContext + Dispatchers.IO)

    private val preferences = Preferences(context)
    private lateinit var queueFile: QueueFile
    private val logsFolder = File(context.filesDir, LOGS_FOLDER_NAME)

    init {
        if (!logsFolder.exists()) {
            logsFolder.mkdir()
        }
        serviceScope.launch {
            val currFileName = preferences.readCurrFileName()
            withContext(Dispatchers.Main) {
                val logFile = File(logsFolder, currFileName)
                queueFile = QueueFile.Builder(logFile).build()
                areObjectsInitialized = true
            }
        }
    }

    fun appendLogToFile(
        priority: LogPriority,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        val logBuilder = StringBuilder()
        logBuilder.append(System.currentTimeMillis())
        logBuilder.append(" ")
        logBuilder.append(logPriorityToString(priority))
        logBuilder.append("/")
        logBuilder.append(tag)
        logBuilder.append(": ")
        logBuilder.append(message)
        if (t != null) {
            logBuilder.append("\n")
            logBuilder.append(Log.getStackTraceString(t))
        }
        logBuilder.append("\n")
        if(areObjectsInitialized) {
            try {
                queueFile.add(logBuilder.toString().toByteArray())
                if(hasDelayedLogsAdded) {
                    queueFile.add(bufferLogList.toString().toByteArray())
                }
            } catch (e: IOException) {
                Timber.d( "Error writing to file: ${e.message}")
            }
            if(hasFileReachedMaxSize()) {
                serviceScope.launch {
                    val currFileName = preferences.setCurrFileName()
                    withContext(Dispatchers.Main) {
                        val logFile = File(logsFolder, currFileName)
                        queueFile = QueueFile.Builder(logFile).build()
                    }
                }
            }
        } else {
            bufferLogList.add("[DELAYED] $logBuilder")
        }
    }

    private fun logPriorityToString(priority: LogPriority): String {
        return when (priority) {
            is LogPriority.Verbose -> "V"
            is LogPriority.Debug -> "D"
            is LogPriority.Info -> "I"
            is LogPriority.Warn -> "W"
            is LogPriority.Error -> "E"
            is LogPriority.Assert -> "A"
            is LogPriority.Null -> "N"
        }
    }

    private fun hasFileReachedMaxSize(): Boolean {
        val fileSizeInKb = queueFile.file().length().div(1024)
        if (fileSizeInKb >= maxFileSize) {
            return true
        }
        return false
    }
}