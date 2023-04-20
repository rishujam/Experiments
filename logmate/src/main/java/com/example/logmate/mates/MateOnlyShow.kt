package com.example.logmate.mates

import timber.log.Timber

/*
 * Created by Sudhanshu Kumar on 19/04/23.
 */

class MateOnlyShow : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
    }
}