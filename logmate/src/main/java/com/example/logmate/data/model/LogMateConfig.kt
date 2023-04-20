package com.example.logmate.data.model

import com.example.logmate.util.LogMateProperties

/*
 * Created by Sudhanshu Kumar on 19/04/23.
 */

/**
 * Model class used while initialisation of LogStore to configure the logger as needed
 */
data class LogMateConfig(
    /** File size in KB*/
    val maxFileSize: Int,
    val syncProperties: LogMateProperties
)
