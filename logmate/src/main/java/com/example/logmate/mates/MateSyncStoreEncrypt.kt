package com.example.logmate.mates

import android.content.Context
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

/*
 * Created by Sudhanshu Kumar on 19/04/23.
 */

internal class MateSyncStoreEncrypt(
    private val context: Context
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
    }
}