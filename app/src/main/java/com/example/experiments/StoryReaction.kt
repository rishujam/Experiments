package com.example.experiments

/*
 * Created by Sudhanshu Kumar on 13/10/23.
 */

data class StoryReaction(
    val udid: String,
    val name: String,
    val speciality: String,
    val profileUrl: String,
    val reaction: StoryReactionType,
    val isConnected: Boolean
) {

    sealed class StoryReactionType {
        object Celebrate : StoryReactionType()
        object Support : StoryReactionType()
        object Like : StoryReactionType()
        object Heart : StoryReactionType()
        object Think : StoryReactionType()
        object None : StoryReactionType()
    }
}


