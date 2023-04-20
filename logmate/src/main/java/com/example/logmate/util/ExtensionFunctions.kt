package com.example.logmate.util

import android.util.Log

/*
 * Created by Sudhanshu Kumar on 21/04/23.
 */

internal fun Int.toLoggerPriority(): LogPriority {
    return when (this) {
        Log.VERBOSE -> LogPriority.Verbose
        Log.DEBUG -> LogPriority.Debug
        Log.INFO -> LogPriority.Info
        Log.WARN -> LogPriority.Warn
        Log.ERROR -> LogPriority.Error
        Log.ASSERT -> LogPriority.Assert
        else -> LogPriority.Null
    }
}