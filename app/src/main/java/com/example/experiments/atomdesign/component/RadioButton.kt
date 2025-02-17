package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.example.experiments.R

class RadioButton
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.radioButtonStyle,
    ) : AppCompatRadioButton(context, attrs, defStyleAttr) {
        init {
            initView()
        }

        private fun initView() {
            buttonDrawable = null
        }

        override fun setChecked(t: Boolean) {
            if (t) {
                buttonDrawable = null
//                this.setBackgroundResource(R.drawable.ic_radio_selected)
            } else {
                buttonDrawable = null
//                this.setBackgroundResource(R.drawable.ic_radio_unselected)
            }
            super.setChecked(t)
        }
    }