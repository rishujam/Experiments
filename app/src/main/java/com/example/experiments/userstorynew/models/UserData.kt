package com.example.experiments.userstorynew.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

@Parcelize
data class UserData(
    val username: String,
    val profilePicUrl: String,
    val noOfStories: Int
) : Parcelable