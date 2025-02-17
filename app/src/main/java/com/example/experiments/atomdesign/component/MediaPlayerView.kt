package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.experiments.databinding.MediaPlayerViewBinding

class MediaPlayerView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding by lazy { MediaPlayerViewBinding.inflate(LayoutInflater.from(context)) }
//        private var uiStatesCallback: ((MediaPlayerViewState) -> Unit)? = null
        private var isMuted = false

        init {
            initView(attrs)
        }

        private fun initView(attrs: AttributeSet?) {
            val layoutParams =
                LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            addView(binding.root, layoutParams)

            binding.ivVideoPlayerViewMute.setOnClickListener {
                isMuted = !isMuted
//                uiStatesCallback?.invoke(MediaPlayerViewState.Mute(isMuted))
            }
        }

        fun getBackgroundImageView() = ImageView(context)

        fun getMediaPlayerView() = binding.exoMediaPlayerView

        fun getPlayButton() = binding.ivMediaPlayerVideoPlay

//        fun togglePlayerView(showThumbnail: Boolean) =
//            binding.apply {
//                ivMediaPlayerVideoPlay.makeInvisible()
//                grpPlayerView.makeVisible()
//            }

        private fun setMuteImage(drawable: Int?) {
            binding.ivVideoPlayerViewMute.apply {
//                drawable?.let { setImageResource(it) }
            }
        }

//        fun setOnUiStatesChangeCallback(callback: ((MediaPlayerViewState) -> Unit)?) {
//            uiStatesCallback = callback
//        }

//        fun setMuteToggle(isMuted: Boolean) {
//            val muteDrawable = if (isMuted) R.drawable.ic_mute_1_0 else R.drawable.ic_unmute_1_0
//            setMuteImage(muteDrawable)
//        }
    }