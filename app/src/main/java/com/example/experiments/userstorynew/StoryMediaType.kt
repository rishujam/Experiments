package com.example.experiments.userstorynew

/*
 * Created by Sudhanshu Kumar on 27/04/23.
 */

sealed class StoryMediaType {
    object Image: StoryMediaType()
    object Video: StoryMediaType()
}
