package com.example.experiments

import android.app.Application
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
//        LogMate.mate(applicationContext, LogMateConfig(40, LogMateProperties.STORE_SHOW))
//        var i = 0
//        while(i < 5) {
//            LogMate.d("current value: i = $i")
//            i++
//        }
    }
}