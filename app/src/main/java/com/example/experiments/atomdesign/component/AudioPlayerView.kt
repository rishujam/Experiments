package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.experiments.R
import com.example.experiments.databinding.AudioPlayerViewBinding
import com.virinchi.atomdesign.component.extensions.makeGone
import com.virinchi.atomdesign.component.extensions.makeInvisible
import com.virinchi.atomdesign.component.extensions.makeVisible

class AudioPlayerView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding by lazy { AudioPlayerViewBinding.inflate(LayoutInflater.from(context)) }
        private var uiStatesCallback: ((AudioPlayerViewState) -> Unit)? = null

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
        }

        private var isSeeking: Boolean = false
        private var playbackDuration: Long = 0
        private var currentPosition: Long = 0

        fun updateProgress(
            progress: Int,
            currentPosition: Long,
        ) {
            this.currentPosition = currentPosition
            if (isSeeking) return
            binding.sbMediaAudio.progress = progress
//            binding.tvMediaAudioTimeDuration.text = currentPosition.formatDuration()
        }

        fun updatePlaybackDuration(playbackDuration: Long) {
            this.playbackDuration = playbackDuration
        }

        fun setOnUiStatesChangeCallback(callback: ((AudioPlayerViewState) -> Unit)?) {
            this.uiStatesCallback = callback
        }

        fun bind() =
            binding.apply {
//                audioUiModel = model

//                updatePlaybackStateUi(model)

                ivMediaAudioPlay.setOnClickListener {
//                    togglePlayback(audioUiModel)
                }

                binding.tvMediaAudioTimeDuration.makeGone()

                sbMediaAudio.setOnSeekBarChangeListener(createSeekBarChangeListener())
            }

        private fun createSeekBarChangeListener(): SeekBar.OnSeekBarChangeListener {
            return object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
//                    if (fromUser && audioUiModel?.playbackState?.isPlaying() == false) {
//                        seekBar?.progress = 0
//                        return
//                    }

                    if (fromUser) {
//                        binding.tvMediaAudioTimeDuration.text = (progress * playbackDuration / 100).formatDuration()
//                        isSeeking = true
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                    if (audioUiModel?.playbackState?.isPlaying() == true && isSeeking) {
//                        val progress = seekBar?.progress ?: 0
//                        val currentSeconds = (progress * playbackDuration) / 100
//                        uiStatesCallback?.invoke(
//                            AudioPlayerViewState.SeekTo(
//                                audioUiModel?.id.orEmpty(),
//                                currentSeconds.toInt(),
//                            ),
//                        )
//                        isSeeking = false
//                    }
                }
            }
        }

//        private fun togglePlayback(model: GRMediaAudioUiModel?) {
//            model?.let {
//                if (model.playbackState.isPlaying()) {
//                    uiStatesCallback?.invoke(
//                        AudioPlayerViewState.Pause(model.id, currentPosition = currentPosition),
//                    )
//                } else {
//                    uiStatesCallback?.invoke(AudioPlayerViewState.Play(model.id))
//                }
//            }
//        }

//        fun updatePlaybackStateUi(model: GRMediaAudioUiModel) {
//            audioUiModel = model
//            when (model.playbackState) {
//                is PlaybackState.Buffering -> updateBufferingUiState()
//                is PlaybackState.Ready -> updateReadyUiState(model.playbackState.playWhenReady)
//                is PlaybackState.Ended -> updateEndedUiState()
//            }
//        }

        private fun updateReadyUiState(isPlaying: Boolean) {
            binding.apply {
//                ivMediaAudioPlay.setImageResource(
//                    if (isPlaying) R.drawable.ic_dd_pause else R.drawable.ic_dd_play,
//                )
                updatePlayingUiState()
            }
        }

        private fun updatePlayingUiState() {
            binding.apply {
                ivMediaAudioPlay.makeVisible()
                pbMediaAudio.makeInvisible()
            }
        }

        private fun updateBufferingUiState() {
            binding.apply {
                tvMediaAudioTimeDuration.apply {
//                    tvMediaAudioTimeDuration.text = audioUiModel?.currentPosition?.formatDuration()
                    makeVisible()
                }
                ivMediaAudioPlay.makeInvisible()
                pbMediaAudio.makeVisible()
            }
        }

        private fun updateEndedUiState() {
            binding.apply {
                sbMediaAudio.progress = 0
                tvMediaAudioTimeDuration.apply {
                    makeGone()
//                    tvMediaAudioTimeDuration.text = EMPTY_STRING
                }
                pbMediaAudio.makeGone()
                ivMediaAudioPlay.apply {
                    makeVisible()
                    setImageResource(R.drawable.ic_dd_play)
                }
            }
        }
    }