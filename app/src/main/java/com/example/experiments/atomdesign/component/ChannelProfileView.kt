package com.example.experiments.atomdesign.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.experiments.atomdesign.component.uistates.ChannelProfileViewStates
import com.example.experiments.databinding.ChannelProfileViewBinding
import com.virinchi.atomdesign.component.constants.ButtonState
import com.virinchi.atomdesign.component.extensions.toggleVisibility
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ChannelProfileView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding by lazy { ChannelProfileViewBinding.inflate(LayoutInflater.from(context)) }

        fun getCta() = binding.ctaPublisher

        var title: String = ""
            set(value) {
                binding.publisherName.text = value
                field = value
            }

        var subTitle: String = ""
            set(value) {
                binding.poweredBy.text = value
                field = value
            }

        var buttonText: String = ""
            set(value) {
                binding.ctaPublisher.setText(value)
                field = value
            }

        // Current state of the button
        var buttonState: ButtonState = ButtonState.SHOW_HIDE

        init {
            initView(attrs)
        }

        private fun initView(attrs: AttributeSet?) {
            val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            addView(binding.root, layoutParams)
        }

        private var _uiStates =
            callbackFlow {
                val followClickListener =
                    OnClickListener {
                        if (isFollowed) return@OnClickListener
                        updateFollowState(!isFollowed)
                        trySend(ChannelProfileViewStates.FollowClicked(!isFollowed))
                    }
                val profileClickListener =
                    OnClickListener {
                        trySend(ChannelProfileViewStates.ProfileClicked)
                    }

                with(binding) {
                    ctaPublisher.setOnClickListener(followClickListener)
                    publisherName.setOnClickListener(profileClickListener)
                    publisherImage.setOnClickListener(profileClickListener)
                    poweredBy.setOnClickListener(profileClickListener)

                    awaitClose {
                        ctaPublisher.setOnClickListener(null)
                        publisherName.setOnClickListener(null)
                        publisherImage.setOnClickListener(null)
                        poweredBy.setOnClickListener(null)
                    }
                }
            }
        val uiStates = _uiStates

        private var isFollowed = false

        fun setFollowState(isFollowed: Boolean) {
            this@ChannelProfileView.isFollowed = isFollowed
            updateFollowState(isFollowed)
        }

        fun updateLogo(drawable: Drawable?) {
            binding.publisherImage.setImageDrawable(drawable)
        }

        fun setPublisherImageBackground(drawable: Drawable?) {
            binding.publisherImage.background = drawable
        }

        fun getImageView() = binding.publisherImage

        private fun updateFollowState(isFollowed: Boolean) {
            if (buttonState == ButtonState.ENABLED_DISABLED) {
                binding.ctaPublisher.setEnabledBg(!isFollowed)
            } else {
                binding.ctaPublisher.toggleVisibility(isFollowed)
            }
        }
    }