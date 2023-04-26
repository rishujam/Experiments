package com.example.experiments.userstorynew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.experiments.userstorynew.StoryFragment
import com.example.experiments.userstorynew.models.UserData

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

class StoryPagerAdapter constructor(fragmentManager: FragmentManager, private val storyList: ArrayList<UserData>)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        StoryFragment.newInstance(position, storyList[position])

    override fun getCount(): Int {
        return storyList.size
    }

    fun findFragmentByPosition(viewPager: ViewPager, position: Int): Fragment? {
        try {
            val f = instantiateItem(viewPager, position)
            return f as? Fragment
        } finally {
            finishUpdate(viewPager)
        }
    }
}