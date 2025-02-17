package com.example.experiments.atomdesign.component.uistates

sealed class ChannelProfileViewStates {
    data class FollowClicked(val isFollowed: Boolean) : ChannelProfileViewStates()

    object ProfileClicked : ChannelProfileViewStates()
}