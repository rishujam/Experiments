package com.example.experiments.userstorynew.utils

import android.content.Context
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

/*
 * Created by Sudhanshu Kumar on 23/05/23.
 */

object VideoCache {

    private var sDownloadCache: SimpleCache? = null
    const val maxCacheSize: Long = 100 * 1024 * 1024

    fun getInstance(context: Context): SimpleCache {
        val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
        if (sDownloadCache == null) sDownloadCache = SimpleCache(File(context.cacheDir, "story_media"), evictor)
        return sDownloadCache as SimpleCache
    }

}