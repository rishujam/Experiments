package com.example.experiments

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView


class DCButton
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        TextView(context, attrs, defStyleAttr) {
    val cornerRadius: Float
    val stokeWidth: Int
    val border: GradientDrawable
    val lessPadding: Boolean
    val buttonMode: Int
    val defaultPadding: Boolean

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.DCButton, 0, 0)
        cornerRadius = array.getDimension(R.styleable.DCButton_corner_radius, resources.getDimension(R.dimen._4dp))
        stokeWidth = array.getDimensionPixelOffset(R.styleable.DCButton_stroke_width, resources.getDimensionPixelOffset(R.dimen._1dp))
        buttonMode = array.getInt(R.styleable.DCButton_button_mode, 0)
        defaultPadding = array.getBoolean(R.styleable.DCButton_defaultPadding, true)
        lessPadding = array.getBoolean(R.styleable.DCButton_lessPadding, false)
        setBackgroundColor(Color.TRANSPARENT)
        border = GradientDrawable()
        updateMode(DCEnumAnnotation(buttonMode))
        gravity = Gravity.CENTER
        array.recycle()
    }

    fun updateMode(mode: DCEnumAnnotation) {
        @DCEnumAnnotation.ButtonMode val status = mode.state
        background = when (status) {
            DCConstant.BUTTON_MODE_PRIMARY -> {
                border.setColor(Color.parseColor(DCColorPicker.PRIMARY))
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }

                isEnabled = true
                border.cornerRadius = cornerRadius
                border
            }
            DCConstant.BUTTON_MODE_SECONDARY -> {
                border.setColor(Color.parseColor(DCColorPicker.SECONDARY))
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
//                isAllCaps=true
                isEnabled = true
                border.cornerRadius = cornerRadius
                border
            }
            DCConstant.BUTTON_MODE_DISABLED -> {
                border.setColor(Color.parseColor(DCColorPicker.GRAY_25))
                isEnabled = false
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border.cornerRadius = cornerRadius
                border
            }
            DCConstant.BUTTON_MODE_PRIMARY_BLANK -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.PRIMARY))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                isEnabled = true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                border
            }
            DCConstant.BUTTON_MODE_SECONDARY_BLANK -> {
               // border.setColor(Color.parseColor(DCColorPicker.WHITE))
                border.setColor(Color.TRANSPARENT)
                setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                /*setTextSize(TypedValue.COMPLEX_UNIT_SP,context.resources.getDimension(R.dimen.txt_size_15sp))
                setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));*/
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                isEnabled = true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.TRANSPARENT))
                border
            }
            DCConstant.BUTTON_MODE_PRIMARY_STROKED_ONLY -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                isEnabled = true
//                isAllCaps=true
                setTextColor(Color.parseColor(DCColorPicker.PRIMARY))
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                border
            }
            DCConstant.BUTTON_MODE_SECONDARY_STROKED_ONLY -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                isEnabled = true
//                isAllCaps=true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                border
            }
            DCConstant.BUTTON_MODE_STROKE_ONLY -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                isEnabled = true
//                isAllCaps=true
                setTextColor(Color.parseColor(DCColorPicker.BLACK))
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                /*TODO changed previous it was 10 changed to 15*/
                if (defaultPadding) {
//                    var padding = resources.getDimensionPixelOffset(R.dimen._10dp)
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            }
            DCConstant.BUTTON_MODE_TEXT_DISABLED -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.GRAY_50))
//                var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
//                setPadding(padding, padding, padding, padding)
                isEnabled = false
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                border
            }
            DCConstant.BUTTON_MODE_TEXT_DISABLED_WITH_BORDER -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.GRAY_50))
//                var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
//                setPadding(padding, padding, padding, padding)
                isEnabled = true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                if (defaultPadding) {
//                    var padding = resources.getDimensionPixelOffset(R.dimen._10dp)
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            }
            DCConstant.BUTTON_MODE_WHITE_TEXT_STROKE_ONLY -> {
                border.setColor(Color.parseColor(DCColorPicker.TRANSPARENT))
                isEnabled = true
//                isAllCaps=true
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            }
            DCConstant.BUTTON_MODE_WHITE_TEXT_BLANK ->{
                border.setColor(Color.parseColor(DCColorPicker.TRANSPARENT))
                isEnabled = true
//                isAllCaps=true
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.TRANSPARENT))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            }
            DCConstant.BUTTON_MODE_TEXT_BLACK_WITHOUT_BACKGROUND -> {
                border.setColor(Color.parseColor(DCColorPicker.TRANSPARENT))
                isEnabled = true
//                isAllCaps=true
                setTextColor(Color.parseColor(DCColorPicker.GRAY_75))
                // border.cornerRadius = cornerRadius
                //border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            }
            DCConstant.BUTTON_MODE_SECONDARY_CORNER_RIGHT_ONLY -> {
                border.setColor(Color.parseColor(DCColorPicker.SECONDARY))
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
//                isAllCaps=true
                isEnabled = true
                border.cornerRadii = floatArrayOf(0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f)
                border
            }
            DCConstant.BUTTON_MODE_DISABLED_STROKE_ONLY -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.GRAY_50))
                isEnabled = false
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                /*TODO changed*/
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._10dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._5dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            }
            DCConstant.BUTTON_MODE_BLACK -> {
                border.setColor(Color.parseColor(DCColorPicker.GRAY_75))
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                isEnabled = true
                border.cornerRadius = cornerRadius
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                /*TODO changed*/
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._10dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._5dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            } DCConstant.BUTTON_MODE_BLACK_WITH_WHITE_BORDER -> {
                border.setColor(Color.parseColor(DCColorPicker.GRAY_75))
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                isEnabled = true
                border.cornerRadius = cornerRadius
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                /*TODO changed*/
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._10dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._5dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                border
            }
            DCConstant.BUTTON_MODE_DIM_TEXT -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.GRAY_50))
//                var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
//                setPadding(padding, padding, padding, padding)
                isEnabled = true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                border
            }
            DCConstant.BUTTON_MODE_GRAY_75 -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.GRAY_75))
//                var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
//                setPadding(padding, padding, padding, padding)
                isEnabled = true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                border
            }
            DCConstant.BUTTON_MODE_GRAY_FILLED_ENABLE -> {
                border.setColor(Color.parseColor(DCColorPicker.GRAY_50))
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
//                var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
//                setPadding(padding, padding, padding, padding)
                isEnabled = true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                border
            }
            DCConstant.BUTTON_MODE_SECONDARY_STROKED -> {
                border.setColor(Color.parseColor(DCColorPicker.TRANSPARENT))
                setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                isEnabled = true
//                isAllCaps=true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SECONDARY))
                border
            }
            DCConstant.BUTTON_MODE_SECONDARY_TEXT_WHITE -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                isEnabled = true
//                isAllCaps=true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.WHITE))
                border
            }
            DCConstant.BUTTON_MODE_TEXT_SECONDARY_WITH_GRAY_BORDER -> {
                border.setColor(Color.parseColor(DCColorPicker.WHITE))
                setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
//                var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
//                setPadding(padding, padding, padding, padding)
                isEnabled = true
                border.cornerRadius = cornerRadius
                border.setStroke(stokeWidth, Color.parseColor(DCColorPicker.SEPERATOR_COLOR))
                border
            }
            DCConstant.BUTTON_MODE_YELLOW_COLOR -> {
                border.setColor(Color.parseColor(DCColorPicker.ERROR))
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                if (defaultPadding) {
                    val padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                isEnabled = true
                border.cornerRadius = cornerRadius
                border
            }
            else -> {
                isEnabled = true
//                isAllCaps=true
                border.setColor(Color.parseColor(DCColorPicker.PRIMARY))
                if (defaultPadding) {
                    var padding = resources.getDimensionPixelOffset(R.dimen._15dp)
                    var paddingHeight = resources.getDimensionPixelOffset(R.dimen._15dp)
                    if (lessPadding)
                        paddingHeight = resources.getDimensionPixelOffset(R.dimen._7dp)
                    setPadding(padding, paddingHeight, padding, paddingHeight)
                }
                setTextColor(Color.parseColor(DCColorPicker.WHITE))
                border.cornerRadius = cornerRadius
                border
            }
        }
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
    }

}
