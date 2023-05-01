package com.example.experiments.userstorynew.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.example.experiments.R
import com.example.experiments.userstorynew.utils.PausableScaleAnimation
import com.example.experiments.userstorynew.utils.hide
import com.example.experiments.userstorynew.utils.show

/*
 * Created by Sudhanshu Kumar on 27/04/23.
 */

class ProgressItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var progressView: View? = null
    private var bgView: View? = null
    private var maxBar: View? = null
    private var animation: PausableScaleAnimation? = null
    private var callback: ProgressCallback? = null
    private var duration = DEFAULT_PROGRESS_DURATION

    init {
        LayoutInflater.from(context).inflate(R.layout.progress_item, this)
        progressView = findViewById(R.id.progress)
        bgView = findViewById(R.id.bgPbItem)
        maxBar = findViewById(R.id.maxBar)
    }

    fun start(duration: Long?, callback: ProgressCallback) {
        duration?.let {
            this.duration = it
        }
        this.callback = callback
        startProgress()
    }

    fun setProgressMax() {
        maxBar?.show()
        maxBar?.setBackgroundColor(context.getColor(R.color.white))
    }

    fun setProgressMin() {
        maxBar?.hide()
        progressView?.hide()
    }

    fun clearAnim() {
        animation?.setAnimationListener(null)
        animation?.cancel()
        animation = null
    }

    private fun startProgress() {
        maxBar?.hide()
        if (duration <= 0) duration = DEFAULT_PROGRESS_DURATION
        animation = PausableScaleAnimation(
            0f,
            1f,
            1f,
            1f,
            Animation.ABSOLUTE,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        animation?.duration = duration
        animation?.interpolator = LinearInterpolator()
        animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {  }

            override fun onAnimationEnd(animation: Animation) {
                callback?.onFinishProgress()
            }

            override fun onAnimationRepeat(animation: Animation) {
                //NO-OP
            }
        })
        animation?.fillAfter = true
        progressView?.startAnimation(animation)
    }

    fun pauseProgress() {
        animation?.pause()
    }

    fun resumeProgress() {
        animation?.resume()
    }

    interface ProgressCallback {
        fun onFinishProgress()
    }

    companion object {
        private const val DEFAULT_PROGRESS_DURATION = 4000L
    }
}

