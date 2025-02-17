package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class SlowDownRecyclerView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : RecyclerView(context, attrs, defStyleAttr) {
        companion object {
            private const val FLING_SPEED_FACTOR = 1f
        }

        override fun fling(
            velocityX: Int,
            velocityY: Int,
        ): Boolean {
            val slowVelocityY = (velocityY * FLING_SPEED_FACTOR).toInt()
            return super.fling(velocityX, slowVelocityY)
        }
    }