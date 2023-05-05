package com.example.experiments.userstorynew.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.experiments.R
import com.example.experiments.userstorynew.StoryFragActions

/*
 * Created by Sudhanshu Kumar on 26/04/23.
 */

class StoryProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private lateinit var progressBars: MutableList<ProgressItem>
    private var storyCount = -1
    private var callback: ProgressFinishCallback? = null

    init {
        orientation = HORIZONTAL
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.StoriesProgressView
        )
        storyCount = typedArray.getInt(R.styleable.StoriesProgressView_progressCount, 0)
        typedArray.recycle()
    }

    fun bindViews(storyCount: Int) {
        removeAllViews()
        progressBars = mutableListOf()
        for (i in 0 until storyCount) {
            val p = createProgressBar()
            progressBars.add(p)
            addView(p)
            if (i + 1 < storyCount) {
                addView(createSpace())
            }
        }
    }

    private fun createProgressBar(): ProgressItem {
        return ProgressItem(context).apply {
            layoutParams = PROGRESS_BAR_LAYOUT_PARAM
        }
    }

    private fun createSpace(): View {
        return View(context).apply {
            layoutParams = SPACE_LAYOUT_PARAM
        }
    }

    fun startProgress(position: Int, duration: Long?, fragCallback: ProgressFinishCallback) {
        callback = fragCallback
        progressBars[position].start(duration, registerFinishCallbackOnProgressItem())
    }

    private fun registerFinishCallbackOnProgressItem(): ProgressItem.ProgressCallback {
        return object : ProgressItem.ProgressCallback {
            override fun onFinishProgress() {
                callback?.onFinishProgress()
            }
        }
    }

    fun handleClick(
        position: Int,
        clickType: StoryFragActions
    ) {
        progressBars[position].clearAnim()
        when (clickType) {
            is StoryFragActions.NextClick -> {
                progressBars[position].setProgressMax()
            }
            is StoryFragActions.PrevClick -> {
                progressBars[position].setProgressMin()
            }
        }
    }

    fun handlePause(position: Int) {
        progressBars[position].pauseProgress()
    }

    fun handleResume(position: Int) {
        progressBars[position].resumeProgress()
    }

    private companion object {
        val PROGRESS_BAR_LAYOUT_PARAM = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1F)
        val SPACE_LAYOUT_PARAM = LayoutParams(5, LayoutParams.WRAP_CONTENT)
    }

    interface ProgressFinishCallback {
        fun onFinishProgress()
    }
}