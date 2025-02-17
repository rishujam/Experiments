package com.virinchi.atomdesign.component.extensions

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.resizeView(
    view: View,
    targetSize: Int,
) {
    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams

    val scale = targetSize.toFloat() / layoutParams.width

    layoutParams.width = (layoutParams.width * scale).toInt()
    layoutParams.height = (layoutParams.height * scale).toInt()
    view.layoutParams = layoutParams

    ConstraintSet().apply {
        clone(this@resizeView)
        applyTo(this@resizeView)
    }
}