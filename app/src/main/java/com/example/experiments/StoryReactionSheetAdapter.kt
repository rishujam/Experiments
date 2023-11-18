package com.example.experiments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.databinding.ItemStoryReactionBinding

/*
 * Created by Sudhanshu Kumar on 14/05/23.
 */

class StoryReactionSheetAdapter :
    RecyclerView.Adapter<StoryReactionSheetAdapter.StoryReactionViewHolder>() {

    inner class StoryReactionViewHolder(
        val binding: ItemStoryReactionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<StoryReaction>() {
        override fun areItemsTheSame(
            oldItem: StoryReaction,
            newItem: StoryReaction
        ): Boolean {
            return oldItem.udid == newItem.udid
        }

        override fun areContentsTheSame(
            oldItem: StoryReaction,
            newItem: StoryReaction
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryReactionViewHolder {
        return StoryReactionViewHolder(
            ItemStoryReactionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: StoryReactionViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            tvNameReactionSheet.text = item.name
            tvSpecialityReactionSheet.text = item.speciality
            if(item.speciality.isEmpty()){
                tvSpecialityReactionSheet.visibility = View.GONE
            }else{
                tvSpecialityReactionSheet.visibility = View.VISIBLE
            }
//            when(item.reaction) {
//                is StoryReactionType.Celebrate -> {
//                    ivReactionStorySheet.setImageDrawable(
//                        AppCompatResources.getDrawable(
//                            ivReactionStorySheet.context,
//                            R.drawable.story_reaction_celebrate
//                        )
//                    )
//                }
//                is StoryReactionType.Support -> {
//                    ivReactionStorySheet.setImageDrawable(
//                        AppCompatResources.getDrawable(
//                            ivReactionStorySheet.context,
//                            R.drawable.story_reaction_support
//                        )
//                    )
//                }
//                is StoryReactionType.Heart -> {
//                    ivReactionStorySheet.setImageDrawable(
//                        AppCompatResources.getDrawable(
//                            ivReactionStorySheet.context,
//                            R.drawable.story_reaction_heart
//                        )
//                    )
//                }
//                is StoryReactionType.Like -> {
//                    ivReactionStorySheet.setImageDrawable(
//                        AppCompatResources.getDrawable(
//                            ivReactionStorySheet.context,
//                            R.drawable.story_reaction_like
//                        )
//                    )
//                }
//                is StoryReactionType.Think -> {
//                    ivReactionStorySheet.setImageDrawable(
//                        AppCompatResources.getDrawable(
//                            ivReactionStorySheet.context,
//                            R.drawable.story_reaction_thinking_btn
//                        )
//                    )
//                }
//                is StoryReactionType.None -> {
//                    ivReactionStorySheet.hide()
//                }
//            }
//            DCGlideHandler.displayBanner(ApplicationLifecycleManager.mActivity,item.profileUrl,ivProfileReactionSheet, R.drawable.default_avatar)
//            ivProfileReactionSheet.setOnClickListener {
//                onProfileClickListener?.let { it(item) }
//            }
        }
    }

    private var onProfileClickListener: ((StoryReaction) -> Unit)? = null
    private var onClickClickListener: ((StoryReaction) -> Unit)? = null
    fun setOnConnectClickListener(clickListener: (StoryReaction) -> Unit) {
        onClickClickListener = clickListener
    }
    fun setOnProfileClickListener(clickListener: (StoryReaction) -> Unit) {
        onProfileClickListener = clickListener
    }
}