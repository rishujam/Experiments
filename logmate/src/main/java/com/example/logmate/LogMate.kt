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

}