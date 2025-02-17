package com.example.experiments.atomdesign.component

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.experiments.R

class EditText
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {
        init {
            background?.let {
                setLineColor(isFocused)
                this.setOnFocusChangeListener { _, b ->
                    setLineColor(b)
                }
            }
        }

        private fun setLineColor(isFocused: Boolean) {
            if (isFocused) {
                background.mutate().setColorFilter(
                    ContextCompat.getColor(context, R.color.color_grey_700),
                    PorterDuff.Mode.SRC_ATOP,
                )
            } else {
                background.mutate().setColorFilter(
                    ContextCompat.getColor(context, R.color.color_grey_300),
                    PorterDuff.Mode.SRC_ATOP,
                )
            }
        }
    }