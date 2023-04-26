package com.example.experiments.userstorynew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

@Parcelize
data class UserList(
    val list: ArrayList<UserData>
) : Parcelable
