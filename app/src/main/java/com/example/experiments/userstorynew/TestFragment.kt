package com.example.experiments.userstorynew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.experiments.MainActivity
import com.example.experiments.databinding.TestFragmentBinding
import com.example.experiments.userstorynew.utils.VideoCache
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource.Factory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Created by Sudhanshu Kumar on 23/05/23.
 */

class TestFragment : Fragment() {

    private var _binding: TestFragmentBinding? = null
    private val binding get() = _binding
    private var exoPlayer: ExoPlayer? = null
    private val mediaSources = mutableListOf<MediaSource>()
    private var position = 0
    private var cachingPosition = 0
    private val fragmentScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private suspend fun preCacheVideo(
        mediaItem: MediaItem,
        cacheDataSource: Factory,
        uri: String,
        storyId: Int
    ) = withContext(Dispatchers.IO) {
        runCatching {
            if (VideoCache.getInstance(requireContext()).isCached(uri, 0, VideoCache.maxCacheSize)) {
                Log.d("RishuTest", "video has been cached, return")
                return@runCatching
            }

            Log.d("RishuTest", "start pre-caching for position: $cachingPosition")
            val downloader = getDownloader(mediaItem, cacheDataSource)

            downloader.download { contentLength, bytesDownloaded, percentDownloaded ->
                if (bytesDownloaded >= VideoCache.maxCacheSize) downloader.cancel()
                Log.d(
                    "RishuTest",
                    "contentLength: $contentLength, bytesDownloaded: $bytesDownloaded, percentDownloaded: $percentDownloaded"
                )
            }
        }.onFailure {
            if (it is InterruptedException) return@onFailure
            Log.d("RishuTest", "Cache fail for position: $cachingPosition with exception: $it}")
            it.printStackTrace()
        }.onSuccess {
            Log.d("RishuTest", "Cache success for position: $cachingPosition")
        }
        Unit
    }

    private fun getDownloader(
        mediaItem: MediaItem,
        cacheDataSource: CacheDataSource.Factory
    ): HlsDownloader {
        return HlsDownloader(mediaItem, cacheDataSource)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TestFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mediaDataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(
            Util.getUserAgent(
                requireContext(),
                "Docquity"
            )
        )
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(VideoCache.getInstance(requireContext()))
            .setUpstreamDataSourceFactory(mediaDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        initPlayerWithCache(cacheDataSourceFactory)
//        initPlayer(mediaDataSourceFactory)

        exoPlayer?.playWhenReady = true
        exoPlayer?.setMediaSource(mediaSources[position])
        exoPlayer?.prepare()

        binding?.btnClose?.setOnClickListener {
            (activity as MainActivity).removeCurrFrag()
        }

        binding?.btnNext?.setOnClickListener {
            if(position + 1 < mediaSources.size) {
                position++
                exoPlayer?.setMediaSource(mediaSources[position])
                exoPlayer?.prepare()
            }
        }

        binding?.btnPrev?.setOnClickListener {
            if(position -1 >=0) {
                position--
                exoPlayer?.setMediaSource(mediaSources[position])
            }

        }
    }

    private fun initPlayerWithCache(factory: CacheDataSource.Factory) {
        val url1 =
            "https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8"
        val url2 =
            "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"
        val url3 =
            "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.mp4/.m3u8"
        val url4 =
            "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
        val list = listOf(url1, url2, url3, url4)
        context?.let { notNullContext ->
            exoPlayer = ExoPlayer.Builder(notNullContext).build()
        }
        binding?.storyDisplayVideo?.player = exoPlayer
        exoPlayer?.playWhenReady = true
        for (i in list) {
            val mediaItem = MediaItem.fromUri(i)
            val mediaSource = HlsMediaSource.Factory(factory)
                .createMediaSource(mediaItem)
            fragmentScope.launch {
                preCacheVideo(mediaItem, factory, i, cachingPosition)
                cachingPosition++
            }
            mediaSources.add(mediaSource)
        }
        val exoListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState == Player.STATE_READY) {
                    binding?.pb?.visibility = View.GONE
                }
                if(playbackState == Player.STATE_BUFFERING) {
                    binding?.pb?.visibility = View.VISIBLE
                }
            }
        }
        exoPlayer?.addListener(exoListener)
    }

    private fun initPlayer(factory: DataSource.Factory) {
        val url1 =
            "http://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8"
        val url2 =
            "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"
        val url3 =
            "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.mp4/.m3u8"
        val url4 =
            "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
        val list = listOf(url1, url2, url3, url4)
        context?.let { notNullContext ->
            exoPlayer = ExoPlayer.Builder(notNullContext).build()
        }
        binding?.storyDisplayVideo?.player = exoPlayer
        exoPlayer?.playWhenReady = true
        for (i in list) {
            val mediaItem = MediaItem.fromUri(i)
            val mediaSource = HlsMediaSource.Factory(factory)
                .createMediaSource(mediaItem)
            mediaSources.add(mediaSource)
        }
        val exoListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState == Player.STATE_READY) {
                    binding?.pb?.visibility = View.GONE
                }
                if(playbackState == Player.STATE_BUFFERING) {
                    binding?.pb?.visibility = View.VISIBLE
                }
            }
        }
        exoPlayer?.addListener(exoListener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}