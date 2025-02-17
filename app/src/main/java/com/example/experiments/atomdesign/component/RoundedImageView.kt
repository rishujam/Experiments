package com.example.experiments.atomdesign.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.example.experiments.R

class RoundedImageView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : AppCompatImageView(context, attrs, defStyleAttr) {
        private val rectF = RectF()
        private val clipPath = Path()
        private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val cornerRadii = FloatArray(8)
        private var borderWidth = 0f
        private var borderColor = Color.BLACK

        init {
            borderPaint.style = Paint.Style.STROKE

            // Read attributes from XML
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)
            val cornerRadius = typedArray.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius, 0)
            val topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.RoundedImageView_topLeftRadius, cornerRadius)
            val topRightRadius = typedArray.getDimensionPixelSize(R.styleable.RoundedImageView_topRightRadius, cornerRadius)
            val bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.RoundedImageView_bottomRightRadius, cornerRadius)
            val bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.RoundedImageView_bottomLeftRadius, cornerRadius)
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundedImageView_borderWidth, 0).toFloat()
            borderColor = typedArray.getColor(R.styleable.RoundedImageView_borderColor, 0)
            typedArray.recycle()

            setCornerRadii(
                topLeftRadius.toFloat(),
                topRightRadius.toFloat(),
                bottomRightRadius.toFloat(),
                bottomLeftRadius.toFloat(),
                cornerRadius.toFloat(),
            )
        }

        fun setCornerRadii(
            topLeftRadius: Float,
            topRightRadius: Float,
            bottomRightRadius: Float,
            bottomLeftRadius: Float,
            cornerRadius: Float,
        ) {
            if (topLeftRadius == 0f && topRightRadius == 0f && bottomRightRadius == 0f && bottomLeftRadius == 0f) {
                // If all individual corner radii are 0, use the overall cornerRadius
                cornerRadii.fill(cornerRadius)
            } else {
                cornerRadii[0] = topLeftRadius
                cornerRadii[1] = topLeftRadius
                cornerRadii[2] = topRightRadius
                cornerRadii[3] = topRightRadius

                cornerRadii[4] = bottomRightRadius
                cornerRadii[5] = bottomRightRadius
                cornerRadii[6] = bottomLeftRadius
                cornerRadii[7] = bottomLeftRadius
            }

            invalidate()
        }

        fun setBorderWidth(width: Float) {
            borderWidth = width
            borderPaint.strokeWidth = borderWidth
            invalidate()
        }

        fun setBorderColor(
            @ColorInt color: Int,
        ) {
            borderColor = color
            borderPaint.color = borderColor
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            rectF.set(0f, 0f, width.toFloat(), height.toFloat())
            clipPath.reset()
            clipPath.addRoundRect(rectF, cornerRadii, Path.Direction.CW)
            canvas.clipPath(clipPath)
            super.onDraw(canvas)
            if (borderWidth > 0) {
                borderPaint.style = Paint.Style.STROKE
                borderPaint.strokeWidth = borderWidth
                borderPaint.color = borderColor
                canvas.drawPath(clipPath, borderPaint)
            }
        }
    }