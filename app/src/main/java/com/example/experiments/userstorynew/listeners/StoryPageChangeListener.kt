package com.example.experiments.userstorynew.listeners

import android.os.Handler
import android.util.Log
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

/*
 * Created by Sudhanshu Kumar on 24/04/23.
 */

abstract class StoryPageChangeListener : ViewPager2.OnPageChangeCallback() {

    private var pageBeforeDragging = 0
    private var currentPage = 0
    private var lastTime = DEBOUNCE_TIMES + 1L

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            ViewPager2.SCROLL_STATE_IDLE -> {
                val now = System.currentTimeMillis()
                if (now - lastTime < DEBOUNCE_TIMES) {
                    return
                }
                lastTime = now
                Handler().postDelayed({
                    if (pageBeforeDragging == currentPage) {
                        onPageScrollCanceled()
                    }
                }, 300L)
            }
            ViewPager2.SCROLL_STATE_DRAGGING -> {
                pageBeforeDragging = currentPage
            }
            ViewPager2.SCROLL_STATE_SETTLING -> {}
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        currentPage = position
    }

    abstract fun onPageScrollCanceled()

    companion object {
        private const val DEBOUNCE_TIMES = 500L
    }
}