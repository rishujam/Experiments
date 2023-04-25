package com.example.experiments.userstory.data

data class Story(val url: String, val storyDate: Long) : java.io.Serializable {

    fun isVideo() =  url.contains(".mp4")
}