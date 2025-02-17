package com.example.experiments.atomdesign.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.experiments.R
import com.example.experiments.databinding.ProfileImageViewLayoutBinding
import com.virinchi.atomdesign.component.extensions.toggleVisibility

class ProfileImageView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding by lazy { ProfileImageViewLayoutBinding.inflate(LayoutInflater.from(context)) }

        init {
            initView(attrs)
        }

        private fun initView(attrs: AttributeSet?) {
            val layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            addView(binding.root, layoutParams)

            attrs?.let {
                val typedArray = context.obtainStyledAttributes(it, R.styleable.ProfileImageView)
                val imageSrc = typedArray.getDrawable(R.styleable.ProfileImageView_piv_image_src)
                val badgeImageSrc =
                    typedArray.getDrawable(R.styleable.ProfileImageView_piv_badge_image_src)
                setBadgeImage(badgeImageSrc)
                typedArray.recycle()
            }
        }



        fun setBadgeImageResource(
            @DrawableRes res: Int,
        ) {
            binding.ivProfileBadge.setImageResource(res)
        }

        fun setBadgeImage(drawable: Drawable?) {
            binding.ivProfileBadge.setImageDrawable(drawable)
        }

        fun setBadgeVisibility(isVisible: Boolean) {
            binding.ivProfileBadge.toggleVisibility(isVisible)
        }
    }