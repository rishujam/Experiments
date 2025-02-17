package com.virinchi.atomdesign.component.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

val View.dm: DisplayMetrics
    get() = resources.displayMetrics

fun Float.pxToDp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val dp = this / (metrics.densityDpi / 160f)
    return dp.roundToInt()
}

fun Float.dpToPx(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val px = this * (metrics.densityDpi / 160f)
    return px.roundToInt()
}

fun Int.pxToDp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val dp = this / (metrics.densityDpi / 160f)
    return dp.roundToInt()
}

fun Int.dpToPx(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val px = this * (metrics.densityDpi / 160f)
    return px.roundToInt()
}

fun View.dpToPx(dp: Int): Int = (dp * this.dm.density + 0.5).toInt()

fun View.pxToDp(px: Int): Int = (px / this.dm.density + 0.5).toInt()

fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0,
    )
}

fun Context.inflate(
    layoutResource: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false,
) {
    LayoutInflater.from(this).inflate(layoutResource, parent, attachToRoot)
}

inline val View.isVisible: Boolean get() = visibility == View.VISIBLE

inline val View.isInvisible: Boolean get() = visibility == View.INVISIBLE

inline val View.isGone: Boolean get() = visibility == View.GONE

fun <T : View> T.makeVisible(): T = apply { visibility = View.VISIBLE }

fun <T : View> T.makeInvisible(): T = apply { visibility = View.INVISIBLE }

fun <T : View> T.makeGone(): T = apply { visibility = View.GONE }

fun <T : View> T.toggleVisibility(isVisible: Boolean): T = apply { if (isVisible) makeVisible() else makeGone() }

fun parseColor(colorString: String?): Int? {
    var colorInt: Int? = null
    try {
        colorInt = Color.parseColor(colorString)
    } catch (e: Exception) {
    }
    return colorInt
}

fun String?.parseColor(default: Int = Color.TRANSPARENT): Int {
    return if (this.isNullOrEmpty()) {
        return default
    } else {
        try {
            Color.parseColor(this)
        } catch (_: Exception) {
            default
        }
    }
}

fun Context.bitmapToDrawable(bitmap: Bitmap): Drawable = BitmapDrawable(this.resources, bitmap)

fun Context.color(
    @ColorRes id: Int,
): Int = ContextCompat.getColor(this, id)

fun Context.dimen(
    @DimenRes id: Int,
): Float = resources.getDimension(id)

fun Context.dimenPixelSize(
    @DimenRes id: Int,
): Int = resources.getDimensionPixelSize(id)

fun Context.drawable(
    @DrawableRes id: Int?,
): Drawable? = id?.let { ContextCompat.getDrawable(this, id) }

fun setDrawableColor(
    drawable: Drawable,
    @ColorRes id: Int,
    context: Context,
) {
    drawable.setColorFilter(ContextCompat.getColor(context, id), PorterDuff.Mode.SRC_IN)
}

/*
 Used for string hexCode as well as color Int conversion
 */
fun <T> generateGradient(
    vararg colors: T?,
    orientation: Orientation,
    radius: Float? = null,
): GradientDrawable {
    val colorIntArray =
        colors.mapNotNull { color ->
            when (color) {
                is String -> color.parseColor()
                is Int -> color
                else -> null
            }
        }.toIntArray()

    return GradientDrawable(orientation, colorIntArray).apply {
        radius?.let { cornerRadius = it }
    }
}

fun generateGradientWithBorder(
    color: Int,
    borderColor: Int,
    radius: Float? = null,
): GradientDrawable {
    return GradientDrawable().apply {
        setColor(color)
        radius?.let { cornerRadius = radius.dpToPx().toFloat() }
        setStroke(1, borderColor)
    }
}

fun generateGradientCornerBottomDrawable(
    backgroundColor: Int,
    borderColor: Int,
): GradientDrawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        setColor(backgroundColor)
        setStroke(0, borderColor)
    }
}

fun View.getBitmap(): Bitmap {
    val bitmap =
        Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888,
        )
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}