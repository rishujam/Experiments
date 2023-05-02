package com.example.experiments.userstorynew

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.experiments.R
import com.example.experiments.databinding.FragmentStory1Binding
import com.example.experiments.userstorynew.listeners.AutoNavigateListener
import com.example.experiments.userstorynew.listeners.OnSwipeTouchListener
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserData
import com.example.experiments.userstorynew.utils.hide
import com.example.experiments.userstorynew.utils.show
import com.example.experiments.userstorynew.views.StoryProgressView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
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
    private val binding get() = _binding

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = arguments?.getParcelable(EXTRA_STORY_USER)
        data?.stories?.size?.let { storySize ->
            binding?.storiesProgressView?.bindViews(storySize)
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
                activity?.finish()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding?.nextStory -> {
                        nextStoryClick()
                    }
                    binding?.prevStory -> {
                        prevStoryClick()
                    }
                }
            }

            override fun onLongClick() {
                pauseStory()
                binding?.group?.hide()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pauseStory()
                    }
                    MotionEvent.ACTION_UP -> {
                        binding?.group?.show()
                        playStory()
                    }
                }
                return false
            }
        }
        binding?.prevStory?.setOnTouchListener(touchListener)
        binding?.nextStory?.setOnTouchListener(touchListener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.autoNavListener = context as AutoNavigateListener
    }

    private fun nextStoryClick() {
        binding?.storiesProgressView?.handleClick(
            storyPosition,
            StoryFragActions.NextClick
        )
        data?.stories?.size?.let {
            if (storyPosition + 1 >= it) {
                autoNavListener?.nextPageNavigate()
            } else {
                storyPosition++
                startStory()
            }
        }
    }

    private fun prevStoryClick() {
        binding?.storiesProgressView?.handleClick(
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
                binding?.apply {
                    storyDisplayVideo.show()
                    storyDisplayImage.hide()
                }
                initPlayer(it.url, it.id)
            } else {
                binding?.apply {
                    storyDisplayVideo.hide()
                    storyDisplayImage.show()
                }
                loadImage(it.url, it.id)
            }
        }
    }

    private fun initPlayer(url: String, storyId: Int) {
        if (exoPlayer != null) {
            exoPlayer?.release()
            exoPlayer = null
        }
        context?.let { notNullContext ->
            exoPlayer = ExoPlayer.Builder(notNullContext).build()
        }
        val mediaDataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(
            Util.getUserAgent(
                requireContext(),
                getString(R.string.app_name)
            )
        )
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url))
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
        binding?.storyDisplayVideo?.setShutterBackgroundColor(Color.BLACK)
        binding?.storyDisplayVideo?.player = exoPlayer

        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(context, "Error while loading: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                if (isLoading) {
                    pauseStory()
                    binding?.storyDisplayVideoProgress?.show()
                } else {
                    playStory()
                    binding?.storyDisplayVideoProgress?.hide()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
                    binding?.storyDisplayVideoProgress?.hide()
                    binding?.storiesProgressView?.startProgress(
                        storyPosition,
                        exoPlayer?.duration,
                        storyFinishCallback
                    )
                    StoryViewedStateManager.addToViewed(Pair(storyId, true))
                }
            }
        })
    }

    private fun loadImage(url: String, id: Int) {
        binding?.storyDisplayImage?.let { imageView ->
            Glide.with(this).load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding?.storyDisplayVideoProgress?.hide()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding?.storyDisplayVideoProgress?.hide()
                        binding?.storiesProgressView?.startProgress(
                            storyPosition,
                            null,
                            storyFinishCallback
                        )
                        StoryViewedStateManager.addToViewed(Pair(id, true))
                        return false
                    }
                })
                .into(imageView)
        }

    }

    private fun pauseStory() {
        exoPlayer?.playWhenReady = false
        binding?.storiesProgressView?.handlePause(storyPosition)
    }

    internal fun playStory() {
        exoPlayer?.playWhenReady = true
        binding?.storiesProgressView?.handleResume(storyPosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        pauseStory()
    }

    override fun onResume() {
        super.onResume()
        startStory()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }
}