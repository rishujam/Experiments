package com.example.experiments.pdf.zoomableimage

import android.view.View

/*
 * Created by Sudhanshu Kumar on 05/09/23.
 */

object Compat {

    fun postOnAnimation(view: View, runnable: Runnable) {
        postOnAnimationJellyBean(view, runnable)
    }

    private fun postOnAnimationJellyBean(view: View, runnable: Runnable) {
        view.postOnAnimation(runnable)
    }
}