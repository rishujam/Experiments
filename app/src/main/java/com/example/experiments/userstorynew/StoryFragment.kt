package com.example.experiments.userstorynew

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.experiments.MainApplication
import com.example.experiments.R
import com.example.experiments.databinding.FragmentStoryBinding
import com.example.experiments.userstorynew.listeners.AutoNavigateListener
import com.example.experiments.userstorynew.listeners.OnSwipeTouchListener
import com.example.experiments.userstorynew.models.Story
import com.example.experiments.userstorynew.models.UserData
import com.example.experiments.userstorynew.utils.hide
import com.example.experiments.userstorynew.utils.show
import com.example.experiments.userstorynew.views.StoryTopProgressBar
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

class StoryFragment : Fragment(), StoryTopProgressBar.StoriesListener {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private var storyUser: UserData? = null
    private val stories: ArrayList<Story> by
    lazy { storyUser!!.stories }

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var pageViewOperator: AutoNavigateListener? = null

    private var storyPosition = 0
    private var pressTime = 0L
    private var limit = 500L

    private var onResumeCalled = false
    private var onVideoPrepared = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyUser = arguments?.getParcelable(EXTRA_STORY_USER)
        binding.storyDisplayVideo.useController = false
        updateStory()
        setUpUi()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.pageViewOperator = context as AutoNavigateListener
    }

    override fun onResume() {
        super.onResume()
        onResumeCalled = true
        if (stories[storyPosition].isVideo() && !onVideoPrepared) {
            simpleExoPlayer?.playWhenReady = false
            return
        }

        simpleExoPlayer?.seekTo(5)
        simpleExoPlayer?.playWhenReady = true
        if (storyPosition == 0) {
            binding.storiesProgressView.startStories()
        } else {
            // restart animation
            storyPosition = StoryActivity.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
            binding.storiesProgressView.startStories(storyPosition)
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.abandon()
    }

    private fun updateStory() {
        simpleExoPlayer?.stop()
        if (stories[storyPosition].isVideo()) {
            binding.storyDisplayVideo.show()
            binding.storyDisplayImage.hide()
            binding.storyDisplayVideoProgress.show()
            initializePlayer()
        } else {
            binding.storyDisplayVideo.hide()
            binding.storyDisplayVideoProgress.hide()
            binding.storyDisplayImage.show()
            Glide.with(this).load(stories[storyPosition].url).into(binding.storyDisplayImage)
        }

//        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
//            timeInMillis = stories[storyPosition].storyDate
//        }
//        binding.storyDisplayTime.text = DateFormat.format("MM-dd-yyyy HH:mm:ss", cal).toString()
    }

    private fun initializePlayer() {
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
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            Uri.parse(stories[storyPosition].url)
        )
        simpleExoPlayer?.prepare(mediaSource, false, false)
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
        }

        binding.storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        binding.storyDisplayVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                binding.storyDisplayVideoProgress.hide()
                if (storyPosition == stories.size.minus(1)) {
                    pageViewOperator?.nextPageNavigate()
                } else {
                    binding.storiesProgressView.skip()
                }
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)
                if (isLoading) {
                    binding.storyDisplayVideoProgress.show()
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                } else {
                    binding.storyDisplayVideoProgress.hide()
                    binding.storiesProgressView.getProgressWithIndex(storyPosition)
                        .setDuration(simpleExoPlayer?.duration ?: 8000L)
                    onVideoPrepared = true
                    resumeCurrentStory()
                }
            }
        })
    }

    private fun setUpUi() {
        val touchListener = object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
                Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.nextStory -> {
                        if (storyPosition == stories.size - 1) {
                            pageViewOperator?.nextPageNavigate()
                        } else {
                            binding.storiesProgressView.skip()
                        }
                    }
                    binding.prevStory -> {
                        if (storyPosition == 0) {
                            pageViewOperator?.backPageNavigate()
                        } else {
                            binding.storiesProgressView.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                binding.group.visibility = View.INVISIBLE
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        binding.group.visibility = View.VISIBLE
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }
        binding.prevStory.setOnTouchListener(touchListener)
        binding.nextStory.setOnTouchListener(touchListener)

        binding.storiesProgressView.setStoriesCountDebug(
            stories.size, position = arguments?.getInt(EXTRA_POSITION) ?: -1
        )
        binding.storiesProgressView.setAllStoryDuration(4000L)
        binding.storiesProgressView.setStoriesListener(this)

        Glide.with(this).load(storyUser?.profilePicUrl).circleCrop()
            .into(binding.ivProfileStoryDetail)
        binding.tvUsernameStoryDetail.text = storyUser?.username
    }

    fun pauseCurrentStory() {
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.pause()
    }

    fun resumeCurrentStory() {
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
            binding.group.visibility = View.VISIBLE
            binding.storiesProgressView.resume()
        }
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
        fun newInstance(position: Int, story: UserData): StoryFragment {
            return StoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_STORY_USER, story)
                }
            }
        }
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        pageViewOperator?.nextPageNavigate()
    }

    override fun onPrev() {
        if (storyPosition <= 0) return
        storyPosition--
        updateStory()
    }

    override fun onNext() {
        if (storyPosition >= stories.size - 1) {
            return
        }
        storyPosition++
        updateStory()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}