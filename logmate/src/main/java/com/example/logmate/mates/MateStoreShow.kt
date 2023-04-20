package com.example.logmate.mates

import android.content.Context
import com.example.logmate.data.FileWriter
import com.example.logmate.util.toLoggerPriority
import timber.log.Timber

/*
 * Created by Sudhanshu Kumar on 19/04/23.
 */

class MateStoreShow(
    private val context: Context,
    private val maxFileSize: Int
) : Timber.Tree() {

    private val fileWriter = FileWriter(context, maxFileSize)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        fileWriter.appendLogToFile(priority.toLoggerPriority(), tag, message, t)
    }

}