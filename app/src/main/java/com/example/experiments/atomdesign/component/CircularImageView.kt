package com.virinchi.atomdesign.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.experiments.R

class CircularImageView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : AppCompatImageView(context, attrs, defStyleAttr) {
        private var borderWidth = 2f
        private var borderColor = Color.WHITE
        private val path = Path()
        private val borderPaint = Paint()

        init {
            scaleType = ScaleType.CENTER_CROP
            initAttributes(context, attrs)
        }

        private fun initAttributes(
            context: Context,
            attrs: AttributeSet?,
        ) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView)
            borderWidth = typedArray.getDimension(R.styleable.CircularImageView_ciBorderWidth, 2f)
            borderColor = typedArray.getColor(R.styleable.CircularImageView_ciBorderColor, Color.WHITE)
            typedArray.recycle()

            borderPaint.style = Paint.Style.STROKE
            borderPaint.strokeWidth = borderWidth
            borderPaint.color = borderColor
        }

        fun setBorderWidth(width: Float) {
            borderWidth = width
            borderPaint.strokeWidth = borderWidth
            invalidate()
        }

        fun setBorderColor(color: Int) {
            borderColor = color
            borderPaint.color = borderColor
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            val width = width
            val height = height

            val centerX = width / 2f
            val centerY = height / 2f
            val radius = centerX.coerceAtMost(centerY) - borderWidth / 2f

            // Reset the path
            path.reset()

            // Create a circular path
            path.addCircle(centerX, centerY, radius, Path.Direction.CW)

            // Clip the canvas to the circular path
            canvas.clipPath(path)

            // Draw the image
            super.onDraw(canvas)

            // Draw the circular border
            canvas.drawCircle(centerX, centerY, radius, borderPaint)
        }
    }