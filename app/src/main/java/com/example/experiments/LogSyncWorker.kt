package com.example.experiments

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.virinchi.logstore.LogEntity
import java.io.File

class LogSyncWorker(
    private val context: Context,
    private val workParams: WorkerParameters
) : CoroutineWorker(context, workParams) {

    override suspend fun doWork(): Result {
        val fileName = inputData.getString("fileName")
        val file = fileName?.let { File(applicationContext.filesDir, it) }
        val logs = file?.readLines()?.map { line ->
            val parts = line.split(",")
            LogEntity(parts[0], parts[1], parts[2], parts[3].toLong())
        }
        //TODO upload data and delete from local
        return Result.success()
    }
}