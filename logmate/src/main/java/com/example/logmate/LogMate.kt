package com.example.logmate

import android.content.Context
import com.example.logmate.data.model.LogMateConfig
import com.example.logmate.mates.MateOnlyShow
import com.example.logmate.mates.MateStoreShow
import com.example.logmate.util.LogMateProperties
import timber.log.Timber

/*
 * Created by Sudhanshu Kumar on 21/04/23.
 */

object LogMate {

    fun mate(
        context: Context,
        loggingConfig: LogMateConfig
    ) {
        when (loggingConfig.syncProperties) {
            LogMateProperties.ONLY_SHOW -> {
                Timber.plant(MateOnlyShow())
            }
            LogMateProperties.STORE_SHOW -> {
                Timber.plant(MateStoreShow(context, loggingConfig.maxFileSize))
            }
            LogMateProperties.STORE_ENCRYPT_SHOW -> {
                //TODO
            }
            LogMateProperties.SYNC_STORE_ENCRYPT -> {
                //TODO
            }
        }
    }

    /** Send a DEBUG log message */
    fun d(msg: String) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).d(msg)
    }

    /** Send a DEBUG log throwable */
    fun d(t: Throwable) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).d(t)
    }

    /** Send a ERROR log message */
    fun e(msg: String) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).e(msg)
    }

    /** Send a ERROR log throwable */
    fun e(t: Throwable) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).e(t)
    }

    /** Send a VERBOSE log message */
    fun v(msg: String) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).v(msg)
    }

    /** Send a VERBOSE log throwable */
    fun v(t: Throwable) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).v(t)
    }

    /** Send a WARNING log throwable */
    fun w(t: Throwable) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).w(t)
    }

    /** Send a WARNING log message */
    fun w(msg: String) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).w(msg)
    }

    /** Send a INFO log message */
    fun i(msg: String) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).i(msg)
    }

    /** Send a INFO log throwable */
    fun i(t: Throwable) {
        val tag = Thread.currentThread().stackTrace[3].className
        Timber.tag(tag).i(t)
    }
}
