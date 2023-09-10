package com.example.experiments.zoomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat


/*
 * Created by Sudhanshu Kumar on 08/09/23.
 */

class ZoomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mScaleFactor = 1f
    private var mScaleDetector: ScaleGestureDetector? = null
    private val mScaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {

        /**
         * This is the active focal point in terms of the viewport. Could be a local
         * variable but kept here to minimize per-frame allocations.
         */
        private var lastSpanX: Float = 0f
        private var lastSpanY: Float = 0f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            scaleX = mScaleFactor
            scaleY = mScaleFactor
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            mScaleFactor = 1f
        }
    }

    init {
        mScaleDetector = ScaleGestureDetector(context, mScaleGestureListener)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mScaleDetector?.onTouchEvent(event) ?: false
    }





}