package com.example.experiments.zoomview

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/*
 * Created by Sudhanshu Kumar on 07/09/23.
 */

internal class ZoomImageView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    companion object {
        private const val MAX_SCALE = 3.0f
        private const val MIN_SCALE = 1.0f
        private const val ZOOM_DURATION = 200
        private const val BASE_ROTATION = 0.0f
    }

    private var mActivePointerId = -1
    private var mBlockParentIntercept = false
    private var mIsDragging = false

    private val mBaseMatrix = Matrix()
    private val mDrawMatrix = Matrix()
    private val mSuppMatrix = Matrix()
    private val mDisplayRect = RectF()
    private val mMatrixValues = FloatArray(9)

    private var mActivePointerIndex = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var mLastTouchX = 0f
    private var mLastTouchY = 0f
    private var mTouchSlop = 0f

    private lateinit var scaleDetector: ScaleGestureDetector

    private val scaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {
        private var lastFocusX = 0f
        private var lastFocusY = 0f
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            if (java.lang.Float.isNaN(scaleFactor) || java.lang.Float.isInfinite(scaleFactor)) return false
            if (scaleFactor >= 0) {
                val focusX = detector.focusX
                val focusY = detector.focusY
                val dx = detector.focusX - lastFocusX
                val dy = detector.focusY - lastFocusY
                temp(scaleFactor, focusX, focusY, dx, dy)
                lastFocusX = detector.focusX
                lastFocusY = detector.focusY
            }
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            lastFocusX = detector.focusX
            lastFocusY = detector.focusY
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {}
    }

    init {
        setOnTouchListener()
        onLayoutChange()
        if (!isInEditMode) {
            val configuration = ViewConfiguration
                .get(context)
            mTouchSlop = configuration.scaledTouchSlop.toFloat()
            scaleDetector = ScaleGestureDetector(context, scaleGestureListener)
        }
        super.setScaleType(ScaleType.MATRIX)
    }

    private fun setOnTouchListener() {
        setOnTouchListener { v, ev ->
            var handled = false
            if (drawable != null) {
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.parent?.requestDisallowInterceptTouchEvent(true)
                    }

                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        if (getScale() < MIN_SCALE) {
                            val rect = getDisplayRect()
                            if (rect != null) {
                                v.post(
                                    AnimatedZoom(
                                        getScale(), MIN_SCALE,
                                        rect.centerX(), rect.centerY()
                                    )
                                )
                            }
                        } else if (getScale() > MAX_SCALE) {
                            val rect = getDisplayRect()
                            if (rect != null) {
                                v.post(
                                    AnimatedZoom(
                                        getScale(), MAX_SCALE,
                                        rect.centerX(), rect.centerY()
                                    )
                                )
                            }
                        }
                        v.performClick()
                    }
                }
                val wasScaling = scaleDetector.isInProgress
                val wasDragging = mIsDragging
                scaleDetector.onTouchEvent(ev)
                handled = processTouchEvent(ev)
                val noScale = !wasScaling && !scaleDetector.isInProgress
                val noDrag = !wasDragging && !mIsDragging
                mBlockParentIntercept = noScale && noDrag
            }
            return@setOnTouchListener handled
        }
    }

    private fun onLayoutChange() {
        addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                updateBaseMatrix()
            }
        }
    }

    private fun temp(scaleFactor: Float, focusX: Float, focusY: Float, dx: Float, dy: Float) {
        if (getScale() < MAX_SCALE || scaleFactor < 1f) {
            mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
            mSuppMatrix.postTranslate(dx, dy)
            if (checkMatrixBounds()) {
                imageMatrix = getDrawMatrix()
            }
        }
    }

    private fun getDisplayRect(): RectF? {
        checkMatrixBounds()
        return getDisplayRect(getDrawMatrix())
    }

    fun getScale(): Float {
        val x = getValue(mSuppMatrix, Matrix.MSCALE_X).toDouble().pow(2.0).toFloat()
        val y = getValue(mSuppMatrix, Matrix.MSKEW_Y).toDouble().pow(2.0).toFloat()
        val square = sqrt((x + y).toDouble())
        return square.toFloat()
    }

    private fun processTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0)
                mVelocityTracker = VelocityTracker.obtain()
                if (null != mVelocityTracker) {
                    mVelocityTracker?.addMovement(ev)
                }
                mLastTouchX = getActiveX(ev)
                mLastTouchY = getActiveY(ev)
                mIsDragging = false
            }

            MotionEvent.ACTION_MOVE -> {
                val x = getActiveX(ev)
                val y = getActiveY(ev)
                val dx = x - mLastTouchX
                val dy = y - mLastTouchY
                if (!mIsDragging) {
                    mIsDragging = sqrt((dx * dx + dy * dy).toDouble()) >= mTouchSlop
                }
                if (mIsDragging) {
                    mLastTouchX = x
                    mLastTouchY = y
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = -1
                if (null != mVelocityTracker) {
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
            }

            MotionEvent.ACTION_UP -> {
                mActivePointerId = -1
                if (mIsDragging) {
                    if (null != mVelocityTracker) {
                        mLastTouchX = getActiveX(ev)
                        mLastTouchY = getActiveY(ev)
                        mVelocityTracker?.addMovement(ev)
                        mVelocityTracker?.computeCurrentVelocity(1000)
                    }
                }
                if (null != mVelocityTracker) {
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = getPointerIndex(ev.action)
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                    mLastTouchX = ev.getX(newPointerIndex)
                    mLastTouchY = ev.getY(newPointerIndex)
                }
            }
        }
        mActivePointerIndex = ev
            .findPointerIndex(if (mActivePointerId != -1) mActivePointerId else 0)
        return true
    }

    private fun getActiveX(ev: MotionEvent): Float {
        return try {
            ev.getX(mActivePointerIndex)
        } catch (e: Exception) {
            ev.x
        }
    }

    private fun getActiveY(ev: MotionEvent): Float {
        return try {
            ev.getY(mActivePointerIndex)
        } catch (e: Exception) {
            ev.y
        }
    }

    private fun getDrawMatrix(): Matrix {
        mDrawMatrix.set(mBaseMatrix)
        mDrawMatrix.postConcat(mSuppMatrix)
        return mDrawMatrix
    }

    private fun getValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    private fun getDisplayRect(matrix: Matrix): RectF? {
        drawable?.let {
            mDisplayRect[0f, 0f, drawable.intrinsicWidth.toFloat()] =
                drawable.intrinsicHeight.toFloat()
            matrix.mapRect(mDisplayRect)
            return mDisplayRect
        }
        return null
    }

    private fun updateBaseMatrix() {
        if (drawable == null) {
            return
        }
        val viewWidth = getImageViewWidth().toFloat()
        val viewHeight = getImageViewHeight().toFloat()
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        mBaseMatrix.reset()
        var mTempSrc = RectF(0f, 0f, drawableWidth.toFloat(), drawableHeight.toFloat())
        val mTempDst = RectF(0f, 0f, viewWidth, viewHeight)
        if (BASE_ROTATION.toInt() % 180 != 0) {
            mTempSrc = RectF(0f, 0f, drawableHeight.toFloat(), drawableWidth.toFloat())
        }
        mBaseMatrix.setRectToRect(
            mTempSrc,
            mTempDst,
            Matrix.ScaleToFit.CENTER
        )
        mSuppMatrix.reset()
        mSuppMatrix.postRotate(BASE_ROTATION % 360)
        imageMatrix = getDrawMatrix()
    }

    private fun checkMatrixBounds(): Boolean {
        val rect = getDisplayRect(getDrawMatrix()) ?: return false
        val height = rect.height()
        val width = rect.width()
        var deltaX = 0f
        var deltaY = 0f
        val viewHeight = getImageViewHeight()
        if (height <= viewHeight) {
            deltaY = (viewHeight - height) / 2 - rect.top
        } else if (rect.top > 0) {
            deltaY = -rect.top
        } else if (rect.bottom < viewHeight) {
            deltaY = viewHeight - rect.bottom
        }
        val viewWidth = getImageViewWidth()
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2 - rect.left
        } else if (rect.left > 0) {
            deltaX = -rect.left
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right
        }
        mSuppMatrix.postTranslate(deltaX, deltaY)
        return true
    }

    private fun getImageViewWidth() = width - paddingLeft - paddingRight

    private fun getImageViewHeight() = height - paddingTop - paddingBottom

    private fun getPointerIndex(action: Int): Int {
        return action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
    }

    inner class AnimatedZoom(
        currentZoom: Float, targetZoom: Float,
        private val mFocalX: Float, private val mFocalY: Float
    ) : Runnable {
        private var mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
        private val mStartTime: Long = System.currentTimeMillis()
        private val mZoomStart: Float
        private val mZoomEnd: Float

        init {
            mZoomStart = currentZoom
            mZoomEnd = targetZoom
        }

        override fun run() {
            val t = interpolate()
            val scale = mZoomStart + t * (mZoomEnd - mZoomStart)
            val deltaScale: Float = scale / getScale()
            temp(deltaScale, mFocalX, mFocalY, 0f, 0f)
            // We haven't hit our target scale yet, so post ourselves again
            if (t < 1f) {
                postOnAnimation(this)
            }
        }

        private fun interpolate(): Float {
            var t: Float = 1f * (System.currentTimeMillis() - mStartTime) / ZOOM_DURATION
            t = min(1f, t)
            t = mInterpolator.getInterpolation(t)
            return t
        }
    }
}