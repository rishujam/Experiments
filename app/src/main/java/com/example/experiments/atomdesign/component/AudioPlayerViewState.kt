package com.example.experiments.atomdesign.component

sealed class AudioPlayerViewState {
//    data class PlayWithMediaItem(
//        val id: String,
//        val playerItem: PlayerMediaItem,
//    ) : AudioPlayerViewState()

    data class Play(val id: String) : AudioPlayerViewState()

    data class Prepare(val id: String) : AudioPlayerViewState()

    data class Pause(val id: String, val currentPosition: Long = 0L) : AudioPlayerViewState()

    data class Stopped(val id: String) : AudioPlayerViewState()

    data class SeekTo(val id: String, val seekTo: Int) : AudioPlayerViewState()

    data class Progress(val id: String, val progress: Int, val currentPosition: Long) : AudioPlayerViewState()

    data class Duration(val id: String, val currentDuration: Long) : AudioPlayerViewState()

//    data class OnPlaybackStateChanged(val id: String, val state: PlaybackState, val currentPosition: Long = 0L) : AudioPlayerViewState()

    data class Error(val error: Exception) : AudioPlayerViewState()

    object Release : AudioPlayerViewState()

    object Idle : AudioPlayerViewState()
}