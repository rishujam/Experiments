package com.example.experiments

import android.app.Application
import com.example.logmate.LogMate
import com.example.logmate.data.model.LogMateConfig
import com.example.logmate.util.LogMateProperties
import kotlin.coroutines.CoroutineContext

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        LogMate.mate(applicationContext, LogMateConfig(40, LogMateProperties.STORE_SHOW))
    }
}