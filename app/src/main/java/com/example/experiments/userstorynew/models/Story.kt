package com.example.experiments.userstorynew.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

@Parcelize
data class Story(
    val id: Int,
    val url: String,
    val storyDate: Long
) : Parcelable {

    fun isVideo() = url.contains(".mp4")
}
