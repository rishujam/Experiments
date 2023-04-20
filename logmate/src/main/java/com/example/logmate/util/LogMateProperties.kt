package com.example.logmate.util

/*
 * Created by Sudhanshu Kumar on 19/04/23.
 */

enum class LogMateProperties {
    /**
     * Encrypt logs, stores locally and sync to server but don't show logs in logcat (PROD BUILD)
     */
    SYNC_STORE_ENCRYPT,

    /** logs will be encrypted only stored locally and will be visible in logcat*/
    STORE_ENCRYPT_SHOW,

    /** logs will be only stored locally and will be visible in logcat*/
    STORE_SHOW,

    /** Logs will be only shown in logcat */
    ONLY_SHOW

}