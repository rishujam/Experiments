package com.example.experiments

import android.app.Application
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import io.branch.referral.Branch

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Branch.enableLogging()
        Branch.getAutoInstance(this)
    }
}