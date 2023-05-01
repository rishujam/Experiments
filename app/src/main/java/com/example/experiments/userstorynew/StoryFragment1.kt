package com.example.experiments.userstorynew

import android.content.Context
import android.graphics.Color
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.experiments.R
import com.example.experiments.databinding.FragmentStory1Binding
import com.example.experiments.userstorynew.listeners.AutoNavigateListener
import com.example.experiments.userstorynew.listeners.OnSwipeTouchListener
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.Story
import com.example.experiments.userstorynew.models.UserData
import com.example.experiments.userstorynew.utils.hide
import com.example.experiments.userstorynew.utils.show
import com.example.experiments.userstorynew.views.StoryProgressView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util

/*
 * Created by Sudhanshu Kumar on 26/04/23.
 */

class StoryFragment1 : Fragment() {

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
        fun newInstance(position: Int, story: UserData): StoryFragment1 {
            return StoryFragment1().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_STORY_USER, story)
                }
            }
        }
    }

    private var _binding: FragmentStory1Binding? = null
    private val binding get() = _binding!!

    private var exoPlayer: ExoPlayer? = null
    private var data: UserData? = null
    private var storyPosition = 0
    private var autoNavListener: AutoNavigateListener? = null
    private lateinit var storyFinishCallback: StoryProgressView.ProgressFinishCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStory1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = arguments?.getParcelable(EXTRA_STORY_USER)
        data?.stories?.size?.let { storySize ->
            binding.storiesProgressView.bindViews(storySize)
            storyFinishCallback = object : StoryProgressView.ProgressFinishCallback {
                override fun onFinishProgress() {
                    nextStoryClick()
                }
            }
        }
        val touchListener = object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
                Toast.makeText(context, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                Toast.makeText(context, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.nextStory -> {
                        nextStoryClick()
                    }
                    binding.prevStory -> {
                        prevStoryClick()
                    }
                }
            }

            override fun onLongClick() {
                pauseStory()
                binding.group.hide()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pauseStory()
                    }
                    MotionEvent.ACTION_UP -> {
                        binding.group.show()
                        playStory()
                    }
                }
                return false
            }
        }
        binding.prevStory.setOnTouchListener(touchListener)
        binding.nextStory.setOnTouchListener(touchListener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.autoNavListener = context as AutoNavigateListener
        Log.d("RishuTest", "is OnAttach")
    }
    private fun nextStoryClick() {
        binding.storiesProgressView.handleClick(
            storyPosition,
            StoryFragActions.NextClick
        )
        data?.stories?.size?.let {
            if(storyPosition + 1 >= it) {
                autoNavListener?.nextPageNavigate()
            } else {
                storyPosition++
                startStory()
            }
        }
    }

    private fun prevStoryClick() {
        binding.storiesProgressView.handleClick(
            storyPosition,
            StoryFragActions.PrevClick
        )
        data?.stories?.size?.let {
            if (storyPosition - 1 < 0) {
                autoNavListener?.backPageNavigate()
            } else {
                storyPosition--
                startStory()
            }
        }
    }

    private fun startStory() {
        data?.stories?.get(storyPosition)?.let {
            if (it.isVideo()) {
                handleViewsVisibilityWhenVideo()
                initPlayer()
            } else {
                handleViewVisibilityWhenImage()
                Glide.with(this).load(it.url).into(binding.storyDisplayImage)
                binding.storiesProgressView.startProgress(storyPosition, null, storyFinishCallback)
            }
        }
    }

    private fun initPlayer() {
        if (exoPlayer != null) {
            exoPlayer?.release()
            exoPlayer = null
        }
        context?.let { notNullContext ->
            exoPlayer = ExoPlayer.Builder(notNullContext).build()
        }
        data?.stories?.get(storyPosition)?.url?.let { notNullUrl ->
            val mediaDataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(
                Util.getUserAgent(
                    requireContext(),
                    getString(R.string.app_name)
                )
            )
            val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(notNullUrl))
            exoPlayer?.setMediaSource(mediaSource)
            exoPlayer?.prepare()
            exoPlayer?.playWhenReady = true
        }
        binding.storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        binding.storyDisplayVideo.player = exoPlayer

        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(context, "Error while loading: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                if (isLoading) {
                    binding.storyDisplayVideoProgress.show()
                } else {
                    binding.storyDisplayVideoProgress.hide()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
                    binding.storiesProgressView.startProgress(storyPosition, exoPlayer?.duration, storyFinishCallback)
                    data?.stories?.get(storyPosition)?.id?.let {
                        StoryViewedStateManager.addToViewed(Pair(it, true))
                    }
                }
            }
        })
    }

    private fun pauseStory() {
        exoPlayer?.playWhenReady = false
        binding.storiesProgressView.handlePause(storyPosition)
    }

    private fun playStory() {
        exoPlayer?.playWhenReady = true
        binding.storiesProgressView.handleResume(storyPosition)
    }

    private fun handleViewsVisibilityWhenVideo() {
        binding.apply {
            storyDisplayVideo.show()
            storyDisplayImage.hide()
            storyDisplayVideoProgress.show()
        }
    }

    private fun handleViewVisibilityWhenImage() {
        binding.apply {
            storyDisplayVideo.hide()
            storyDisplayVideoProgress.hide()
            storyDisplayImage.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        pauseStory()
        Log.d("RishuTest", "in onPause")
    }

    override fun onStart() {
        super.onStart()
        Log.d("RishuTest", "is OnStart")
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        Log.d("RishuTest", "in onResume")
        startStory()
    }
}