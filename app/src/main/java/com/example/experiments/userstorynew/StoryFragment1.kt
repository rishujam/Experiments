package com.example.experiments.userstorynew

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
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
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

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var data: UserData? = null
    private var storyPosition = 0
    private var autoNavListener: AutoNavigateListener? = null

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

        startStory()

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
                binding.storiesProgressView.handlePause(storyPosition)
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

    private fun nextStoryClick() {
        storyPosition++
        data?.stories?.size?.let {
            if(storyPosition >= it) {
                autoNavListener?.nextPageNavigate()
            } else {
                startStory(storyPosition-1, storyPosition, StoryFragActions.NEXT_CLICK)
            }
        }
    }

    private fun prevStoryClick() {
        storyPosition--
        data?.stories?.size?.let {
            if(storyPosition < 0) {
                autoNavListener?.backPageNavigate()
            } else {
                startStory(storyPosition+1, storyPosition, StoryFragActions.PREV_CLICK)
            }
        }
    }

    private fun startStory() {
        data?.stories?.get(storyPosition)?.let { story ->
            if(story.isVideo()) {
                binding.storyDisplayVideo.show()
                binding.storyDisplayImage.hide()
                binding.storyDisplayVideoProgress.show()
                initPlayer()
            } else {
                binding.storyDisplayVideo.hide()
                binding.storyDisplayVideoProgress.hide()
                binding.storyDisplayImage.show()
                Glide.with(this).load(story.url).into(binding.storyDisplayImage)
            }
        }
    }

    private fun startStory(oldPosition: Int, newPosition: Int, clickType: StoryFragActions) {
        data?.stories?.get(storyPosition)?.let {
            if(it.isVideo()) {
                binding.storyDisplayVideo.show()
                binding.storyDisplayImage.hide()
                binding.storyDisplayVideoProgress.show()
                initPlayer()
                binding.storiesProgressView.handleClick(
                    oldPosition,
                    newPosition,
                    clickType
                )
            } else {
                binding.storyDisplayVideo.hide()
                binding.storyDisplayVideoProgress.hide()
                binding.storyDisplayImage.show()
                Glide.with(this).load(it.url).into(binding.storyDisplayImage)
                binding.storiesProgressView.handleClick(
                    oldPosition,
                    newPosition,
                    clickType
                )
                binding.storiesProgressView.startStory(newPosition, null)
            }
        }
    }

    private fun initPlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
        } else {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
        }
        mediaDataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(
                requireContext(),
                Util.getUserAgent(requireContext(), getString(R.string.app_name))
            )
        )
        data?.stories?.get(storyPosition)?.url?.let {
            val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                Uri.parse(it)
            )
            simpleExoPlayer?.prepare(mediaSource, false, false)
            simpleExoPlayer?.playWhenReady = true
        }

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(context, "Error while loading: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)
                if(isLoading) {
                    binding.storyDisplayVideoProgress.show()
                } else {
                    binding.storyDisplayVideoProgress.hide()
                    data?.stories?.get(storyPosition)?.id?.let {
                        StoryViewedStateManager.addToViewed(Pair(it, true))
                    }
                }
            }
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY && playWhenReady) {
                    binding.storiesProgressView.startStory(storyPosition, simpleExoPlayer?.duration)
                }
            }
        })
    }

    private fun pauseStory() {
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.handlePause(storyPosition)
    }

    private fun playStory() {
        simpleExoPlayer?.playWhenReady = true
        binding.storiesProgressView.handleResume(storyPosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}