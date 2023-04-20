package com.example.logmate.util

import android.util.Log

/*
 * Created by Sudhanshu Kumar on 20/04/23.
 */

sealed class LogPriority(p: Int?) {
    object Verbose : LogPriority(2)
    object Debug : LogPriority(3)
    object Info : LogPriority(4)
    object Warn : LogPriority(5)
    object Error : LogPriority(6)
    object Assert : LogPriority(7)
    object Null: LogPriority(null)
}