package com.example.experiments.userstorynew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.experiments.userstorynew.StoryFragment
import com.example.experiments.userstorynew.models.UserData

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

class StoryPagerAdapter(
    fragmentManager: FragmentActivity,
    private val storyList: ArrayList<UserData>
) : FragmentStateAdapter(fragmentManager) {

    private val fragments = hashMapOf<Int, StoryFragment>()

    override fun getItemCount(): Int = storyList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = StoryFragment.newInstance(storyList[position].username)
        fragments[position] = fragment
        return fragment
    }

    fun findFragmentByPosition(position: Int): Fragment? {
        return fragments[position]
    }

    fun clearFragments() {
        for(i in fragments.values) {
            i.onDestroy()
        }
        fragments.clear()
    }
}