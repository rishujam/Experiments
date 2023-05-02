package com.example.experiments.userstorynew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.experiments.userstorynew.StoryFragment1
import com.example.experiments.userstorynew.models.UserData

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

class StoryPagerAdapter(
    fragmentManager: FragmentActivity,
    private val storyList: ArrayList<UserData>
) : FragmentStateAdapter(fragmentManager) {

    private val fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = storyList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = StoryFragment1.newInstance(position, storyList[position])
        fragments.add(fragment)
        return fragment
    }

    fun findFragmentByPosition(position: Int): Fragment {
        return fragments[position]
    }

    fun clearFragments() {
        for(i in fragments) {
            i.onDestroy()
        }
        fragments.clear()
    }
}