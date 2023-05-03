package com.example.experiments.userstorynew.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.databinding.ItemHomeRvBinding
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserData
import com.example.experiments.userstorynew.utils.StoryGen

/*
 * Created by Sudhanshu Kumar on 25/04/23.
 */

class StoryThumbnailAdapter(
    private val list: List<UserData>
) : RecyclerView.Adapter<StoryThumbnailAdapter.StoryThumbnailViewHolder>() {

    inner class StoryThumbnailViewHolder(val binding: ItemHomeRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryThumbnailAdapter.StoryThumbnailViewHolder {
        return StoryThumbnailViewHolder(
            ItemHomeRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: StoryThumbnailAdapter.StoryThumbnailViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            val userData = list[position]
            if (isAllStoriesViewed(userData.username)) {
                tvViewed.visibility = View.VISIBLE
            }
            storyThumbnail.setOnClickListener {
                onItemClickListener?.let { it(position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    private fun isAllStoriesViewed(userName: String): Boolean {
        val storiesByUser = StoryGen.getStoriesOfUser(userName)
        for (i in storiesByUser) {
            if (!StoryViewedStateManager.isViewed(i.id)) {
                return false
            }
        }
        return true
    }
}