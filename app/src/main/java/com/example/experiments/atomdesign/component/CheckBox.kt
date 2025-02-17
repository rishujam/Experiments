package com.virinchi.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.example.experiments.R

class CheckBox
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatCheckBox(context, attrs) {
        private var isEnabled = true

        init {
            initView()
        }

        private fun initView() {
            buttonDrawable = null
        }

        override fun setChecked(isChecked: Boolean) {
            buttonDrawable = null

//            val backgroundResId =
//                when {
//                    isChecked && isEnabled -> R.drawable.ic_checkbox_checked_enabled
//                    isChecked && !isEnabled -> R.drawable.ic_checkbox_checked_disabled
//                    !isChecked && isEnabled -> R.drawable.ic_checkbox_unchecked_enabled
//                    else -> R.drawable.ic_checkbox_unchecked_disabled
//                }
//
//            setBackgroundResource(backgroundResId)
            super.setChecked(isChecked)
        }

        fun updateDisableCheckBox(isEnabled: Boolean) {
            this.isEnabled = isEnabled
        }
    }