package com.example.experiments.userstorynew

/*
 * Created by Sudhanshu Kumar on 27/04/23.
 */

sealed class StoryFragActions {
    object NEXT_CLICK : StoryFragActions()
    object PREV_CLICK : StoryFragActions()
}