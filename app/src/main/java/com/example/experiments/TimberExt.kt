package com.example.experiments

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.experiments.FileLoggingTree
import com.example.experiments.LogSyncWorker
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit


object TimberExt {

    var testI: TestI? = null

    fun plantTree(fileName: String, context: Context, enableSync: Boolean) {
        if(enableSync) {
            Timber.plant(FileLoggingTree(context, fileName))
            //registerWorker(context, fileName)
        } else {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun registerWorker(context: Context, fileName: String) {
        val workManager = WorkManager.getInstance(context)
        val constraint = Constraints.Builder()
            .setRequiresDeviceIdle(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val data = Data.Builder()
        data.putString("fileName", fileName)
        val syncRequest = PeriodicWorkRequestBuilder<LogSyncWorker>(4, TimeUnit.HOURS)
            .setInputData(data.build())
            .setConstraints(constraint)
            .setInitialDelay(2, TimeUnit.MINUTES)
            .build()
        workManager
            .enqueueUniquePeriodicWork(
                "logsupload",
                ExistingPeriodicWorkPolicy.REPLACE,
                syncRequest
            )
    }

    fun unregisterWorker(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("logsupload")
    }
}