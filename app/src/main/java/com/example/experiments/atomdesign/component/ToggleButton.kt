package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.core.content.ContextCompat
import com.example.experiments.R

class ToggleButton
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.buttonStyleToggle,
    ) : AppCompatToggleButton(context, attrs, defStyleAttr) {
        init {
            textOn = ""
            textOff = ""
//            background = ContextCompat.getDrawable(context, R.drawable.bg_switch_track)
        }
    }