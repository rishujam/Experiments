package com.example.experiments.atomdesign.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.R
import kotlin.math.abs

class RecyclerViewDotsIndicator
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
    ) : View(context, attrs, defStyle) {
        private companion object {
            const val DEFAULT_DOT_COUNT = 5
            const val DEFAULT_FADING_DOT_COUNT = 1
            const val DEFAULT_DOT_RADIUS_DP = 4
            const val DEFAULT_SELECTED_DOT_RADIUS_DP = 5.5f
            const val DEFAULT_DOT_SEPARATION_DISTANCE_DP = 10
        }

        private val typedArray =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.IndefinitePagerIndicator,
                0,
                0,
            )

        private val dotCount by lazy {
            typedArray.getInteger(
                R.styleable.IndefinitePagerIndicator_dotCount,
                DEFAULT_DOT_COUNT,
            )
        }
        private val fadingDotCount by lazy {
            typedArray.getInt(
                R.styleable.IndefinitePagerIndicator_fadingDotCount,
                DEFAULT_FADING_DOT_COUNT,
            )
        }
        private val dotRadiusPx by lazy {
            typedArray.getDimensionPixelSize(
                R.styleable.IndefinitePagerIndicator_dotRadius,
                dpToPx(DEFAULT_DOT_RADIUS_DP.toFloat()),
            )
        }

        private val selectedDotRadiusPx =
            lazy {
                typedArray.getDimensionPixelSize(
                    R.styleable.IndefinitePagerIndicator_dotRadius,
                    dpToPx(DEFAULT_SELECTED_DOT_RADIUS_DP.toFloat()),
                )
            }

        private val dotColor by lazy {
            typedArray.getColor(
                R.styleable.IndefinitePagerIndicator_dotColor,
                ContextCompat.getColor(context, R.color.default_dot_color),
            )
        }

        private val selectedDotColor by lazy {
            typedArray.getColor(
                R.styleable.IndefinitePagerIndicator_selectedDotColor,
                ContextCompat.getColor(context, R.color.default_selected_dot_color),
            )
        }

        private val dotSeparationDistancePx by lazy {
            typedArray.getDimensionPixelSize(
                R.styleable.IndefinitePagerIndicator_dotSeparation,
                dpToPx(DEFAULT_DOT_SEPARATION_DISTANCE_DP.toFloat()),
            )
        }

        private val selectedDotPaint by lazy { getDefaultPaintConfig(defaultColor = selectedDotColor) }
        private val dotPaint by lazy { getDefaultPaintConfig(defaultColor = dotColor) }

        private var recyclerView: RecyclerView? = null
        private var internalRecyclerScrollListener: InternalRecyclerScrollListener? = null
        private val interpolator = DecelerateInterpolator()

        private var selectedItemPosition = 0
        private var intermediateSelectedItemPosition = 0
        private var offsetPercent = 0f

        init {
            initializePaints()
        }

        private fun initializePaints() {
            selectedDotPaint.apply { style = Paint.Style.FILL }
            dotPaint.apply { style = Paint.Style.FILL }
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            (0 until getItemCount())
                .map { position ->
                    getDotCoordinate(position)
                }
                .forEach { coordinate ->
                    val (xPosition: Float, yPosition: Float) = getXYPositionsByCoordinate(coordinate)
                    canvas.drawCircle(xPosition, yPosition, getRadius(coordinate), getPaint(coordinate))
                }
        }

        override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int,
        ) {
            val minimumViewSize = 2 * (selectedDotRadiusPx.value)
            setMeasuredDimension(getCalculatedWidth(), minimumViewSize)
        }

        fun attachToRecyclerView(
            recyclerView: RecyclerView?,
            @ColorInt dotColor: Int? = null,
            @ColorInt selectedDotColor: Int? = null,
        ) {
            removeAllSources()
            selectedDotPaint.apply {
                selectedDotColor?.let {
                    color = selectedDotColor
                }
            }
            dotPaint.apply {
                dotColor?.let {
                    color = dotColor
                }
            }

            this.recyclerView = recyclerView
            internalRecyclerScrollListener = InternalRecyclerScrollListener()
            internalRecyclerScrollListener?.let {
                this.recyclerView?.addOnScrollListener(it)
            }
        }

        private fun getXYPositionsByCoordinate(coordinate: Float): Pair<Float, Float> {
            val xPosition: Float = width / 2 + coordinate
            val yPosition: Float = getDotYCoordinate().toFloat()
            return Pair(xPosition, yPosition)
        }

        private fun getDefaultPaintConfig(
            defaultStyle: Paint.Style = Paint.Style.FILL,
            isAntiAliasDefault: Boolean = true,
            @ColorInt defaultColor: Int,
        ): Paint =
            Paint().apply {
                style = defaultStyle
                isAntiAlias = isAntiAliasDefault
                color = defaultColor
            }

        private fun dpToPx(dp: Float) = (dp * resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toInt()

        private fun getItemCount() = recyclerView?.adapter?.itemCount ?: 0

        private fun isRtl() = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL

        private fun getRTLPosition(position: Int) = getItemCount() - position - 1

        private fun getDotCoordinate(position: Int) =
            (position - intermediateSelectedItemPosition) * getDistanceBetweenTheCenterOfTwoDots() +
                (getDistanceBetweenTheCenterOfTwoDots() * offsetPercent)

        private fun getDotYCoordinate() = selectedDotRadiusPx.value

        private fun getDistanceBetweenTheCenterOfTwoDots() = 2 * dotRadiusPx + dotSeparationDistancePx

        private fun getRadius(coordinate: Float): Float {
            val coordinateAbs = abs(coordinate)
            val largeDotThreshold = dotCount.toFloat() / 2 * getDistanceBetweenTheCenterOfTwoDots()
            return when {
                coordinateAbs < getDistanceBetweenTheCenterOfTwoDots() / 2 -> selectedDotRadiusPx.value.toFloat()
                coordinateAbs <= largeDotThreshold -> dotRadiusPx.toFloat()
                else -> {
                    val percentTowardsEdge =
                        (coordinateAbs - largeDotThreshold) /
                            (getCalculatedWidth() / 2.01f - largeDotThreshold)
                    interpolator.getInterpolation(1 - percentTowardsEdge) * dotRadiusPx
                }
            }
        }

        private fun getPaint(coordinate: Float) =
            when {
                abs(coordinate) < getDistanceBetweenTheCenterOfTwoDots() / 2 -> selectedDotPaint
                else -> dotPaint
            }

        private fun getCalculatedWidth(): Int {
            val maxNumVisibleDots = dotCount + 2 * fadingDotCount
            return (maxNumVisibleDots - 1) * getDistanceBetweenTheCenterOfTwoDots() + 2 * dotRadiusPx
        }

        private fun removeAllSources() {
            internalRecyclerScrollListener?.let {
                recyclerView?.removeOnScrollListener(it)
            }
            recyclerView = null
        }

        internal inner class InternalRecyclerScrollListener : RecyclerView.OnScrollListener() {
            private var previousMostVisibleChild: View? = null

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
            ) {
                val view = getMostVisibleChild()
                if (view != null) {
                    setIntermediateSelectedItemPosition(view)
                    offsetPercent = view.left.toFloat() / view.measuredWidth
                }

                with(recyclerView.layoutManager as LinearLayoutManager) {
                    val visibleItemPosition =
                        if (dx >= 0) findLastVisibleItemPosition() else findFirstVisibleItemPosition()

                    if (previousMostVisibleChild !== findViewByPosition(visibleItemPosition)) {
                        selectedItemPosition = intermediateSelectedItemPosition
                    }
                }

                previousMostVisibleChild = view
                invalidate()
            }

            private fun getMostVisibleChild(): View? {
                var mostVisibleChild: View? = null
                var mostVisibleChildPercent = 0f
                for (i in recyclerView?.layoutManager?.childCount!! - 1 downTo 0) {
                    val child = recyclerView?.layoutManager?.getChildAt(i)
                    if (child != null) {
                        val percentVisible = calculatePercentVisible(child)
                        if (percentVisible >= mostVisibleChildPercent) {
                            mostVisibleChildPercent = percentVisible
                            mostVisibleChild = child
                        }
                    }
                }

                return mostVisibleChild
            }

            private fun calculatePercentVisible(child: View): Float {
                val left = child.left
                val right = child.right
                val width = child.width

                return when {
                    left < 0 -> right / width.toFloat()
                    right > getWidth() -> (getWidth() - left) / width.toFloat()
                    else -> 1f
                }
            }

            private fun setIntermediateSelectedItemPosition(mostVisibleChild: View) {
                recyclerView?.findContainingViewHolder(mostVisibleChild)?.adapterPosition
                    ?.let { position ->
                        intermediateSelectedItemPosition =
                            if (isRtl()) {
                                getRTLPosition(position)
                            } else {
                                position
                            }
                    }
            }
        }
    }