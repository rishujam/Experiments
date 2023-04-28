package com.example.experiments.userstorynew.managers

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

/** This will be destroyed whenever data is refreshed from backend */
object StoryViewedStateManager {

    //map of username to isViewed
    var viewedMap: HashMap<Int, Boolean>? = null

    fun init() {
        viewedMap = hashMapOf()
    }

    fun addToViewed(pair: Pair<Int, Boolean>) {
        viewedMap?.put(pair.first, pair.second)
    }

    fun isViewed(userName: Int): Boolean {
        return viewedMap?.get(userName) ?: false
    }

    fun destroy() {
        viewedMap?.clear()
        viewedMap = null
    }

}