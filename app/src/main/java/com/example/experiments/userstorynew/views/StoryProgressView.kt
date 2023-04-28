package com.example.experiments.userstorynew.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Toast
import com.example.experiments.R
import com.example.experiments.userstorynew.StoryFragActions
import com.example.experiments.userstorynew.StoryMediaType

/*
 * Created by Sudhanshu Kumar on 26/04/23.
 */

class StoryProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val progressBars = mutableListOf<ProgressItem>()
    private val storyNavigateListener: StoryNavigateListener? = null
    private var storyItemProgressCallback: ProgressItem.ProgressCallback? = null
    private var storyCount = -1


    init {
        orientation = HORIZONTAL
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.StoriesProgressView
        )
        storyCount = typedArray.getInt(R.styleable.StoriesProgressView_progressCount, 0)
        typedArray.recycle()
        bindViews()
        registerCallback()
    }

    private fun bindViews() {
        progressBars.clear()
        removeAllViews()
        for (i in 0 until storyCount) {
            val p = createProgressBar()
            progressBars.add(p)
            addView(p)
        }
    }

    private fun createProgressBar(): ProgressItem {
        return ProgressItem(context).apply {
            layoutParams = PROGRESS_BAR_LAYOUT_PARAM
        }
    }

    private fun registerCallback() {
        storyItemProgressCallback = object : ProgressItem.ProgressCallback {
            override fun onStartProgress() {

            }
            override fun onFinishProgress() {

            }
        }
    }

    fun startStory(position: Int, duration: Long?) {
        progressBars[position].start(duration)
    }

    fun handleClick(
        oldPosition: Int,
        newPosition: Int,
        clickType: StoryFragActions
    ) {
        when(clickType) {
            is StoryFragActions.NEXT_CLICK -> {
                progressBars[oldPosition].updateProgress(android.view.ViewGroup.LayoutParams.MATCH_PARENT)
            }
            is StoryFragActions.PREV_CLICK -> {
                progressBars[oldPosition].updateProgress(0)
                progressBars[newPosition].updateProgress(0)
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

    interface StoryNavigateListener {
        /** When user clicks right side */
        fun onNext()

        /** When user clicks left side */
        fun onPrev()

        /** When story completes without user interaction */
        fun onCompletion()
    }
}