package com.example.experiments

import android.content.Context
import android.util.Log
import com.squareup.tape2.QueueFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class FileLoggingTree(context: Context, filename: String) : Timber.DebugTree() {

    private val logFile: File = File(context.filesDir, filename)
    var queueFile: QueueFile = QueueFile.Builder(logFile).build()

    private val coroutineContext: CoroutineContext = Job()
    private val coroutineScope = CoroutineScope(coroutineContext + Dispatchers.IO)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        try {
            val logBuilder = StringBuilder()
            logBuilder.append(System.currentTimeMillis())
            logBuilder.append(", ")
            logBuilder.append(priorityToString(priority))
            logBuilder.append(", ")
            logBuilder.append(tag)
            logBuilder.append(", ")
            logBuilder.append(message)
            logBuilder.append("\n")
            coroutineScope.launch {
                queueFile.add(logBuilder.toString().toByteArray())
            }
        } catch (e: IOException) {
            Timber.e(e, "Error writing to file")
        }
    }

    private fun priorityToString(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "?"
        }
    }
}