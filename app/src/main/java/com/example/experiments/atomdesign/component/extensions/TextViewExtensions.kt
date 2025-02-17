package com.virinchi.atomdesign.component.extensions

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.experiments.atomdesign.component.TextView
import com.virinchi.atomdesign.component.constants.DrawablePosition

fun TextView.setDrawable(
    drawable: Drawable?,
    position: DrawablePosition,
) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(
        if (position == DrawablePosition.LEFT) drawable else null,
        if (position == DrawablePosition.TOP) drawable else null,
        if (position == DrawablePosition.RIGHT) drawable else null,
        if (position == DrawablePosition.BOTTOM) drawable else null,
    )
}

fun TextView.setDrawableWithResourceId(
    @DrawableRes drawable: Int?,
    position: DrawablePosition = DrawablePosition.LEFT,
) {
    drawable?.let {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            if (position == DrawablePosition.LEFT) drawable else 0,
            if (position == DrawablePosition.TOP) drawable else 0,
            if (position == DrawablePosition.RIGHT) drawable else 0,
            if (position == DrawablePosition.BOTTOM) drawable else 0,
        )
    }
}

fun TextView.textColor(
    @ColorRes color: Int,
) {
    setTextColor(ContextCompat.getColor(context, color))
}

fun TextView.setDrawableToggle(
    isSelected: Boolean,
    @DrawableRes selectedResId: Int,
    @DrawableRes unSelectedResId: Int,
    position: DrawablePosition,
) {
    setDrawableWithResourceId(
        if (isSelected) selectedResId else unSelectedResId,
        position,
    )
}

fun TextView.setDrawableSize(
    width: Int,
    height: Int,
    position: DrawablePosition,
) {
    val drawable = compoundDrawables[position.position]
    drawable?.let {
        it.setBounds(0, 0, width, height)
        setDrawable(it, position)
    }
}

fun TextView.setDrawableTintColor(tintColor: Int) {
    val colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
    val drawables = compoundDrawablesRelative
    for (drawable in drawables) {
        drawable?.colorFilter = colorFilter
    }
}

fun TextView.setTextOrGoneIfEmpty(string: String?) {
    if (string?.isPurelyEmpty() == true) {
        makeGone()
    } else {
        text = string
    }
}