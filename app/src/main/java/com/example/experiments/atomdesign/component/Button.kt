package com.example.experiments.atomdesign.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import com.example.experiments.R
import com.example.experiments.databinding.ButtonLayoutBinding
import com.virinchi.atomdesign.component.ViewConstant
import com.virinchi.atomdesign.component.extensions.setDrawableTintColor

class Button
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {
        private var drawableSrc: Drawable? = null

        @DrawableRes
        private var drawableResId: Int? = null
        private var drawableTint: ColorStateList? = null
        private var drawableMode: Int? = null
        private var buttonSize: Int? = null
        private var buttonType: Int? = null
        private var text: String? = ""
        private var isButtonEnabled: Boolean
        private var binding: ButtonLayoutBinding? = null
        private var listener: OnClickListener? = null
        private var drawablePadding = resources.getDimensionPixelSize(R.dimen.dimen_8dp)

        init {
            val array = context.obtainStyledAttributes(attrs, R.styleable.Button)
            val drawable = array.getResourceId(R.styleable.Button_drawableSrc, 0)
            if (drawable != 0) {
                drawableSrc = AppCompatResources.getDrawable(context, drawable)!!
            }
            drawableTint = array.getColorStateList(R.styleable.Button_drawableTint)
            drawableMode = array.getInt(R.styleable.Button_drawableGravity, 1)
            buttonSize = array.getInt(R.styleable.Button_atomButtonSize, 1)
            buttonType = array.getInt(R.styleable.Button_buttonType, 1)
            isButtonEnabled = array.getBoolean(R.styleable.Button_isButtonEnabled, true)
            text = array.getString(R.styleable.Button_textValue)
            array.recycle()
            init()
        }

        private fun init() {
            binding = ButtonLayoutBinding.inflate(LayoutInflater.from(context))
            removeAllViews()
            updateStyles()
            updateLayoutParams()
            updateTextDrawable()
            updateText()
            addView(binding?.root)
        }

        private fun updateText() {
            binding?.btnText?.text = text
            if ((this@Button).text?.isNotBlank() == true && (this@Button).text?.isNotEmpty() == true) {
                binding?.btnText?.compoundDrawablePadding = drawablePadding
            }
        }

        private fun updateTextDrawable() {
            drawableSrc?.let {
                if (drawableMode == ViewConstant.Button.BUTTON_DRAWABLE_START) {
                    binding?.btnText?.setCompoundDrawablesWithIntrinsicBounds(it, null, null, null)
                } else {
                    binding?.btnText?.setCompoundDrawablesWithIntrinsicBounds(null, null, it, null)
                }
            }

            drawableTint?.let {
                updateDrawableTint(it)
            }
        }

        fun updateTextDrawableWithDrawableRes(
            @DrawableRes drawable: Int,
        ) {
            drawableResId = drawable
            drawableResId?.let {
                if (drawableMode == ViewConstant.Button.BUTTON_DRAWABLE_START) {
                    binding?.btnText?.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0)
                } else {
                    binding?.btnText?.setCompoundDrawablesWithIntrinsicBounds(0, 0, it, 0)
                }
            }

            drawableTint?.let {
                updateDrawableTint(it)
            }
        }

        private fun updateLayoutParams() {
            val layoutParams =
                when (buttonSize) {
                    ViewConstant.Button.BUTTON_SIZE_LARGE -> {
                        LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            resources.getDimensionPixelSize(R.dimen.large_btn_height),
                        )
                    }

                    ViewConstant.Button.BUTTON_SIZE_MEDIUM -> {
                        LayoutParams(
                            resources.getDimensionPixelSize(R.dimen.medium_btn_width),
                            resources.getDimensionPixelSize(R.dimen.medium_btn_height),
                        )
                    }

                    ViewConstant.Button.BUTTON_SIZE_SMALL_EXTENDED -> {
                        LayoutParams(
                            resources.getDimensionPixelSize(R.dimen.small_btn_extended_width),
                            resources.getDimensionPixelSize(R.dimen.small_btn_extended_height),
                        )
                    }

                    ViewConstant.Button.BUTTON_SIZE_WRAP_CONTENT -> {
                        LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT,
                        ).apply {
                            binding?.btnText?.setPadding(
                                0,
                                resources.getDimensionPixelSize(R.dimen.dimen_8dp),
                                0,
                                resources.getDimensionPixelSize(R.dimen.dimen_8dp),
                            )
                        }
                    }

                    ViewConstant.Button.BUTTON_SIZE_SMALL_WRAP_CONTENT -> {
                        LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            resources.getDimensionPixelSize(R.dimen.small_btn_height),
                        )
                    }

                    else -> {
                        LayoutParams(
                            resources.getDimensionPixelSize(R.dimen.small_btn_width),
                            resources.getDimensionPixelSize(R.dimen.small_btn_height),
                        )
                    }
                }
            binding?.root?.layoutParams = layoutParams

            when (buttonType) {
                ViewConstant.Button.BUTTON_PRIMARY -> {
                    binding?.btnText?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_primary_white,
                        ),
                    )

                    val bgResource =
                        if (isButtonEnabled) {
                            when (buttonSize) {
                                ViewConstant.Button.BUTTON_SIZE_LARGE, ViewConstant.Button.BUTTON_SIZE_MEDIUM -> {
                                    R.drawable.bg_btn_primary
                                }

                                else -> {
                                    R.drawable.bg_btn_primary_small_btn
                                }
                            }
                        } else {
                            when (buttonSize) {
                                ViewConstant.Button.BUTTON_SIZE_LARGE, ViewConstant.Button.BUTTON_SIZE_MEDIUM -> {
                                    R.drawable.bg_grey_300_radius_12
                                }

                                else -> {
                                    R.drawable.bg_grey_300_radius_8
                                }
                            }
                        }
                    binding?.root?.setBackgroundResource(bgResource)
                }

                else -> {
                    val bgResource =
                        if (isButtonEnabled) {
                            when (buttonSize) {
                                ViewConstant.Button.BUTTON_SIZE_LARGE, ViewConstant.Button.BUTTON_SIZE_MEDIUM -> {
                                    R.drawable.btn_secondary_no_icon
                                }

                                else -> {
                                    R.drawable.btn_secondary_small_no_icon
                                }
                            }
                        } else {
                            when (buttonSize) {
                                ViewConstant.Button.BUTTON_SIZE_LARGE, ViewConstant.Button.BUTTON_SIZE_MEDIUM -> {
                                    R.drawable.bg_stroke_grey_300_radius_12
                                }

                                else -> {
                                    R.drawable.bg_rounded_stroke_grey_300_radius_8dp
                                }
                            }
                        }
                    binding?.root?.setBackgroundResource(bgResource)
                    binding?.btnText?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            if (isButtonEnabled) R.color.color_primary_black else R.color.color_grey_300,
                        ),
                    )
                }
            }
        }

        private fun updateElevation(buttonEnabled: Boolean) {
            binding?.root?.let {
                if (buttonEnabled) {
                    ViewCompat.setElevation(it, 8f)
                } else {
                    ViewCompat.setElevation(it, 0f)
                }
            }
        }

        private fun updateStyles() {
            binding?.btnText?.let {
                with(it) {
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    if ((this@Button).text?.isNotBlank() == true && (this@Button).text?.isNotEmpty() == true) {
                        compoundDrawablePadding = drawablePadding
                    }
                    TextViewCompat.setTextAppearance(this, R.style.Typography_Docquity_Button)
                }
            }
        }

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            if (isButtonEnabled && (event.action == MotionEvent.ACTION_UP)) {
                listener?.onClick(this)
            }
            return super.dispatchTouchEvent(event)
        }

        override fun dispatchKeyEvent(event: KeyEvent): Boolean {
            if (isButtonEnabled && (event.action == KeyEvent.ACTION_UP) &&
                (
                    event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                        event.keyCode == KeyEvent.KEYCODE_ENTER
                )
            ) {
                listener?.onClick(this)
            }
            return super.dispatchKeyEvent(event)
        }

        override fun setOnClickListener(listener: OnClickListener?) {
            this.listener = listener
        }

        fun getText() = binding?.btnText

        fun setText(text: String?) {
            this.text = text
            binding?.btnText?.text = text
            if ((this@Button).text?.isNotBlank() == true && (this@Button).text?.isNotEmpty() == true) {
                binding?.btnText?.compoundDrawablePadding = drawablePadding
            }
        }

        fun setDrawableSrc(drawableId: Int) {
            drawableSrc = AppCompatResources.getDrawable(context, drawableId)
            updateTextDrawable()
        }

        fun setDrawableSrcWithDrawableRes(
            @DrawableRes startDrawableId: Int,
            @DrawableRes endDrawableId: Int,
        ) {
            binding?.btnText?.setCompoundDrawablesWithIntrinsicBounds(
                startDrawableId,
                0,
                endDrawableId,
                0,
            )
        }

        fun setDrawableSrc(
            startDrawableId: Int,
            endDrawableId: Int,
        ) {
            val startSrc = AppCompatResources.getDrawable(context, startDrawableId)
            val endSrc = AppCompatResources.getDrawable(context, endDrawableId)
            binding?.btnText?.setCompoundDrawablesWithIntrinsicBounds(startSrc, null, endSrc, null)
        }

        fun updateDrawablePadding(padding: Int) {
            drawablePadding = padding
            binding?.btnText?.compoundDrawablePadding = drawablePadding
        }

        fun setEnabledBg(isEnabled: Boolean) {
            isButtonEnabled = isEnabled
            updateLayoutParams()
            this.isClickable = isEnabled
            this.rootView.isClickable = isEnabled
        }

        fun updateButtonType(isPrimaryButton: Boolean) {
            buttonType = if (isPrimaryButton) 1 else 2
            updateLayoutParams()
        }

        fun updateButtonSize(size: Int) {
            buttonSize = size
            updateLayoutParams()
        }

        fun updateDrawableTint(colorStateList: ColorStateList) {
            binding?.btnText?.let { it1 ->
                TextViewCompat.setCompoundDrawableTintList(
                    it1,
                    colorStateList,
                )
            }
        }

        fun setGradientBackground(gradientDrawable: GradientDrawable) {
            binding?.root?.setBackgroundResource(0)
            binding?.root?.background = gradientDrawable
        }

        fun setFont(font: Int) {
            getText()?.typeface = ResourcesCompat.getFont(context, font)
        }

        fun setTextColor(color: Int) {
            getText()?.setTextColor(color)
        }

        fun setDrawableTintColor(color: Int) {
            getText()?.setDrawableTintColor(color)
        }

        fun clearBackground() {
            binding?.root?.background = null
            binding?.btnText?.setPadding(0)
            binding?.btnText?.compoundDrawablePadding =
                resources.getDimensionPixelSize(R.dimen.dimen_4dp)
            updateDrawableTint(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.color_primary_black,
                    ),
                ),
            )
            setTextColor(ContextCompat.getColor(context, R.color.color_primary_black))
        }
    }