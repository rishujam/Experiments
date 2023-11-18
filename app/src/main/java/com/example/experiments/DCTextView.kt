package com.example.experiments

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView


class DCTextView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextView(context, attrs, defStyleAttr) {
    private val TAG = "DCTextView"

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.DCTextView, 0, 0)
        val textMode = array.getInt(R.styleable.DCTextView_text_mode, 0)
        val defaultTypeLinkCheck = array.getBoolean(R.styleable.DCTextView_default_link_check, true)
        // Log.e(TAG, "defaultTypeLinkCheck is $defaultTypeLinkCheck")
//        if (defaultTypeLinkCheck) {
//            Linkify.addLinks(this, Linkify.WEB_URLS);
//            setLinksClickable(true);
        setLinkTextColor(Color.parseColor(DCColorPicker.SECONDARY))
//            setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6.0f, resources.displayMetrics), 1.0f)
//        }
        updateMode(DCEnumAnnotation(textMode))
        array.recycle()
    }

    fun updateModeAsLink() {
        Linkify.addLinks(this, Linkify.WEB_URLS)
        setLinksClickable(true)
        setLinkTextColor(Color.parseColor(DCColorPicker.SECONDARY))
        setLineSpacing(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                6.0f,
                resources.displayMetrics
            ), 1.0f
        )
    }

    fun setSpannableParagraphBoldAndParagraphRegularGray50(
        boldText: String?, regularText: String?,
        isSpaceRequiredBetween: Boolean? = true
    ) {
        // var fontSize = resources.getDimension(R.dimen.txt_size_12)
        var fontSize = 12f
        setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        var finalText = when (isSpaceRequiredBetween!!) {
            true -> "$boldText $regularText"
            false -> "$boldText$regularText"
        }
        var boldTextLength = boldText?.length!!
        var regularTextStartIndex = boldTextLength + 1
        var finalLength = finalText.length
        val font = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        val font2 = Typeface.create("sans-serif", Typeface.NORMAL)
        val SS = SpannableString(finalText)
        SS.setSpan(
            DCCustomTypefaceSpan("", font, Color.parseColor(DCColorPicker.BLACK)),
            0,
            boldTextLength,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        SS.setSpan(
            DCCustomTypefaceSpan("", font2, Color.parseColor(DCColorPicker.GRAY_50)),
            regularTextStartIndex,
            finalLength,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        setText(SS)
    }

    fun updateMode(mode: DCEnumAnnotation) {
        @DCEnumAnnotation.TextMode val status = mode.state
        when (status) {
            DCConstant.TEXT_MODE_HEADING_MEDIUM -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_20)
                    var fontSize = 20f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
//                    typeface = Typeface.create("sans-serif-bold", Typeface.NORMAL)
//                    typeface = Typeface.DEFAULT_BOLD
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.BLACK))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_BOLD -> {
                try {
                    //  var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.DEFAULT_BOLD
                    setTextColor(Color.parseColor(DCColorPicker.BLACK))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_REGULAR -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.BLACK))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM_WHITE -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.WHITE))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM_GRAY_75 -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.GRAY_75))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM_SECONDARY -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM_PRIMARY -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.PRIMARY))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.BLACK))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_REGULAR_GRAY_50 -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.GRAY_50))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_BOLD -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    var fontSize = 12f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.BOLD)
                    setTextColor(Color.parseColor(DCColorPicker.BLACK))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_REGULAR_GRAY_50 -> {
                try {
                    //  var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    var fontSize = 12f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.GRAY_50))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_REGULAR -> {
                try {
                    var fontSize = 12f
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.BLACK))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_REGULAR_SECONDARY -> {
                try {
                    var fontSize = 12f
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_GRAY_75 -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    var fontSize = 12f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.GRAY_75))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_WHITE -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    var fontSize = 12f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.WHITE))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_SECONDARY -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    var fontSize = 12f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_CONSENT -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    val fontSize = 12f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.CONSENT_GRAY))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_ERROR -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_12)
                    var fontSize = 12f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.ERROR))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SMALL_MEDIUM_GRAY_75 -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_10)
                    var fontSize = 10f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.GRAY_75))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            DCConstant.TEXT_MODE_SUB_HEADING_REGULAR_SECONDARY -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.SECONDARY))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_LARGE_MEDIUM_WHITE -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 39f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.WHITE))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SMALL_MEDIUM_WHITE -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 10f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.WHITE))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SUB_HEADING_REGULAR_WHITE -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.WHITE))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_SMALL_REGULAR_WHITE -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 10f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.WHITE))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            DCConstant.TEXT_MODE_HEADING_EDITOR_CHOICE -> {
                try {
                    // var fontSize = resources.getDimension(R.dimen.txt_size_15)
                    var fontSize = 15f
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    setTextColor(Color.parseColor(DCColorPicker.EC_TITLE_COLOR))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
                /* try {
                     var fontSize = resources.getDimension(R.dimen.txt_size_12)
                     setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                     typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                     setTextColor(Color.parseColor(DCColorPicker.BLACK))
                 } catch (e: Exception) {
                     e.printStackTrace()
                 }*/
            }
        }
    }

    fun updateTextColor(textColor: Int) {
        setTextColor(textColor)
    }
}
