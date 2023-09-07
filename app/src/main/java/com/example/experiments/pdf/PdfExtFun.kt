package com.example.experiments.pdf

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.experiments.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Created by Sudhanshu Kumar on 06/09/23.
 */

fun View.showPagesViewForSomeSeconds(
    scope: CoroutineScope,
    context: Context?
) {
    this.show()
    scope.launch {
        delay(1500L)
        withContext(Dispatchers.Main) {
            val fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out_anim)
            fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    this@showPagesViewForSomeSeconds.gone()
                }
            })
            this@showPagesViewForSomeSeconds.startAnimation(fadeOutAnimation)
        }
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}