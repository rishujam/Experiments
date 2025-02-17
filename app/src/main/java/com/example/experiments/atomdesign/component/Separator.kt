package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.experiments.R
import com.virinchi.atomdesign.component.ViewConstant
import com.virinchi.atomdesign.component.extensions.parseColor

class Separator
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : View(context, attrs, defStyleAttr) {
        private var separatorWidth: Int
        private var separatorOrientation = ViewConstant.Separator.Type.HORIZONTAL
        private val colorGrey = ContextCompat.getColor(context, R.color.color_grey_100)

        init {
            val array =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.Separator,
                    0,
                    0,
                )
            separatorWidth =
                array.getDimensionPixelSize(
                    R.styleable.Separator_separator_width,
                    resources.getDimensionPixelSize(R.dimen.dimen_1dp),
                )
            separatorOrientation =
                array.getInt(
                    R.styleable.Separator_separator_orientation,
                    ViewConstant.Separator.Type.HORIZONTAL,
                )
            array.recycle()
            setBackgroundColor(colorGrey)
        }

        override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int,
        ) {
            val finalWidth: Int
            val finalHeight: Int
            if (separatorOrientation == ViewConstant.Separator.Type.VERTICAL) {
                finalWidth = separatorWidth
                finalHeight = MeasureSpec.getSize(heightMeasureSpec)
            } else {
                finalWidth = MeasureSpec.getSize(widthMeasureSpec)
                finalHeight = separatorWidth
            }
            super.setMeasuredDimension(finalWidth, finalHeight)
        }

        fun setSeparatorColor(
            @ColorRes id: Int,
        ) {
            setBackgroundColor(ContextCompat.getColor(context, id))
        }

        fun setSeparatorColor(
            color: Int?,
            defaultColor: Int = colorGrey,
        ) {
            setBackgroundColor(color ?: defaultColor)
        }

        fun setSeparatorColor(color: String) {
            setBackgroundColor(color.parseColor(R.color.color_grey_100))
        }
    }