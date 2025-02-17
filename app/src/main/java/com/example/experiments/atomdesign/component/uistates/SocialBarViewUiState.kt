package com.example.experiments.atomdesign.component.uistates

sealed class SocialBarViewUiState {
    data class LikeClicked(val isLiked: Boolean) : SocialBarViewUiState()

    object CommentClicked : SocialBarViewUiState()

    object ShareClicked : SocialBarViewUiState()

    object RepostClicked : SocialBarViewUiState()

    object BookmarkClicked : SocialBarViewUiState()

    object DownloadClicked : SocialBarViewUiState()
}