package com.example.experiments.adapterCheck

import android.content.Context
import android.os.Trace
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.data.TestData
import com.example.experiments.databinding.DelegateLoadingLayoutBinding
import com.example.experiments.databinding.GrAddPostItemLayoutBinding
import com.example.experiments.databinding.GrArticlesItemLayoutBinding
import com.example.experiments.databinding.GrCmeItemLayoutBinding
import com.example.experiments.databinding.GrCommunityCardLayoutBinding
import com.example.experiments.databinding.GrCommunityPublicCarouselLayoutBinding
import com.example.experiments.databinding.GrDoctalkItemLayoutBinding
import com.example.experiments.databinding.GrDrugSampleLayoutBinding
import com.example.experiments.databinding.GrEditorChoiceLayoutBinding
import com.example.experiments.databinding.GrEventLayoutBinding
import com.example.experiments.databinding.GrFeedItemLayoutV1Binding
import com.example.experiments.databinding.GrGameCardLayoutBinding
import com.example.experiments.databinding.GrHealthCardDefaultItemLayoutBinding
import com.example.experiments.databinding.GrHealthCardEditableItemLayoutBinding
import com.example.experiments.databinding.GrHealthCardFestiveItemLayoutBinding
import com.example.experiments.databinding.GrHealthCardItemLayoutBinding
import com.example.experiments.databinding.GrNoStoryCardBinding
import com.example.experiments.databinding.GrNudgeVerifyGenericType1Binding
import com.example.experiments.databinding.GrNudgeVerifyGenericType2Binding
import com.example.experiments.databinding.GrRepostFeedItemLayoutBinding
import com.example.experiments.databinding.GrStoryCardBinding
import com.example.experiments.databinding.GrWebinarItemLayoutBinding
import com.example.experiments.databinding.RecyclerViewTitleLayoutBinding
import com.virinchi.feature.composite.grandround.ada.ArticleAdapter
import com.virinchi.feature.composite.grandround.ada.BannerAdapter
import com.virinchi.feature.composite.grandround.ada.CmeAdapter
import com.virinchi.feature.composite.grandround.ada.CommunityAdapter
import com.virinchi.feature.composite.grandround.ada.ConnectionAdapter
import com.virinchi.feature.composite.grandround.ada.DoctalkAdapter
import com.virinchi.feature.composite.grandround.ada.DrugSampleAdapter
import com.virinchi.feature.composite.grandround.ada.EditorChoiceAdapter
import com.virinchi.feature.composite.grandround.ada.EventAdapter
import com.virinchi.feature.composite.grandround.ada.GameAdapter
import com.virinchi.feature.composite.grandround.ada.HealthCardAdapter
import com.virinchi.feature.composite.grandround.ada.HealthCardDefaultAdapter
import com.virinchi.feature.composite.grandround.ada.HealthCardEditableAdapter
import com.virinchi.feature.composite.grandround.ada.HealthCardFestiveAdapter
import com.virinchi.feature.composite.grandround.ada.LoadingAdapter
import com.virinchi.feature.composite.grandround.ada.NoStoryViewHolder
import com.virinchi.feature.composite.grandround.ada.NudgeCommunityAdapter
import com.virinchi.feature.composite.grandround.ada.NudgeGenericType1Adapter
import com.virinchi.feature.composite.grandround.ada.NudgeGenericType2Adapter
import com.virinchi.feature.composite.grandround.ada.PharmaAdapter
import com.virinchi.feature.composite.grandround.ada.QuickActionAdapter
import com.virinchi.feature.composite.grandround.ada.RepostAdapter
import com.virinchi.feature.composite.grandround.ada.StoryAdapter
import com.virinchi.feature.composite.grandround.adapterCheck.GRFeedViewHolder
import com.virinchi.feature.composite.grandround.adapterCheck.GRWebinarViewHolder

class CompositeAdapterV2(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<TestData>()

    fun submitList(newList: List<TestData>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<TestData>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    companion object {
        private const val TYPE_1 = 1
        private const val TYPE_2 = 2
        private const val TYPE_3 = 3
        private const val TYPE_4 = 4
        private const val TYPE_5 = 5
        private const val TYPE_6 = 6
        private const val TYPE_7 = 7
        private const val TYPE_8 = 8
        private const val TYPE_9 = 9
        private const val TYPE_10 = 10
        private const val TYPE_11 = 11
        private const val TYPE_12 = 12
        private const val TYPE_13 = 13
        private const val TYPE_14 = 14
        private const val TYPE_15 = 15
        private const val TYPE_16 = 16
        private const val TYPE_17 = 17
        private const val TYPE_18 = 18
        private const val TYPE_19 = 19
        private const val TYPE_20 = 20
        private const val TYPE_21 = 21
        private const val TYPE_22 = 22
        private const val TYPE_23 = 23
        private const val TYPE_24 = 24
        private const val TYPE_25 = 25
        private const val TYPE_26 = 26

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e("ScrollLogs", "onCreateViewHolder")
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_1 -> {
                Trace.beginSection("Scroll: Feed")
                val binding = GrFeedItemLayoutV1Binding.inflate(inflater, parent, false)
                GRFeedViewHolder(binding)
            }

            TYPE_2 -> {
                Trace.beginSection("Scroll: Webinar")
                val binding = GrWebinarItemLayoutBinding.inflate(inflater, parent, false)
                Trace.endSection()
                GRWebinarViewHolder(binding)
            }

            TYPE_3 -> {
                val binding = GrArticlesItemLayoutBinding.inflate(inflater, parent, false)
                ArticleAdapter(binding)
            }

            TYPE_4 -> {
                val binding = GrCmeItemLayoutBinding.inflate(inflater, parent, false)
                CmeAdapter(binding)
            }

            TYPE_5 -> {
                val binding = GrCommunityCardLayoutBinding.inflate(inflater, parent, false)
                CommunityAdapter(binding)
            }

            TYPE_6 -> {
                Trace.beginSection("Scroll: Doctalk")
                val binding = GrDoctalkItemLayoutBinding.inflate(inflater, parent, false)
                DoctalkAdapter(binding)
            }

            TYPE_7 -> {
                val binding = GrEditorChoiceLayoutBinding.inflate(inflater, parent, false)
                EditorChoiceAdapter(binding)
            }

            TYPE_8 -> {
                val binding = GrEventLayoutBinding.inflate(inflater, parent, false)
                EventAdapter(binding)
            }

            TYPE_9 -> {
                val binding = GrGameCardLayoutBinding.inflate(inflater, parent, false)
                GameAdapter(binding)
            }

            TYPE_10 -> {
                val binding = GrHealthCardItemLayoutBinding.inflate(inflater, parent, false)
                HealthCardAdapter(binding)
            }

            TYPE_11 -> {
                val binding = GrHealthCardDefaultItemLayoutBinding.inflate(inflater, parent, false)
                HealthCardDefaultAdapter(binding)
            }

            TYPE_12 -> {
                val binding = GrHealthCardEditableItemLayoutBinding.inflate(inflater, parent, false)
                HealthCardEditableAdapter(binding)
            }

            TYPE_13 -> {
                val binding = GrHealthCardFestiveItemLayoutBinding.inflate(inflater, parent, false)
                HealthCardFestiveAdapter(binding)
            }

            TYPE_14 -> {
                val binding = DelegateLoadingLayoutBinding.inflate(inflater, parent, false)
                LoadingAdapter(binding)
            }

            TYPE_15 -> {
                val binding = GrNoStoryCardBinding.inflate(inflater, parent, false)
                NoStoryViewHolder(binding)
            }

            TYPE_16 -> {
                val binding = GrCommunityPublicCarouselLayoutBinding.inflate(inflater, parent, false)
                NudgeCommunityAdapter(binding)
            }

            TYPE_17 -> {
                Trace.beginSection("Scroll: Repost")
                val binding = GrRepostFeedItemLayoutBinding.inflate(inflater, parent, false)
                Trace.endSection()
                RepostAdapter(binding)
            }

            TYPE_18 -> {
                val binding = GrStoryCardBinding.inflate(inflater, parent, false)
                StoryAdapter(binding)
            }

            TYPE_19 -> {
                val binding = GrAddPostItemLayoutBinding.inflate(inflater, parent, false)
                AddToPostViewHolder(binding)
            }

            TYPE_20 -> {
                val binding = RecyclerViewTitleLayoutBinding.inflate(inflater, parent, false)
                BannerAdapter(binding)
            }

            TYPE_21 -> {
                val binding = RecyclerViewTitleLayoutBinding.inflate(inflater, parent, false)
                ConnectionAdapter(binding)
            }

            TYPE_22 -> {
                val binding = GrDrugSampleLayoutBinding.inflate(inflater, parent, false)
                DrugSampleAdapter(binding)
            }

            TYPE_23 -> {
                val binding = GrNudgeVerifyGenericType1Binding.inflate(inflater, parent, false)
                NudgeGenericType1Adapter(binding)
            }

            TYPE_24 -> {
                val binding = GrNudgeVerifyGenericType2Binding.inflate(inflater, parent, false)
                NudgeGenericType2Adapter(binding)
            }

            TYPE_25 -> {
                val binding = RecyclerViewTitleLayoutBinding.inflate(inflater, parent, false)
                PharmaAdapter(binding)
            }

            TYPE_26 -> {
                val binding = RecyclerViewTitleLayoutBinding.inflate(inflater, parent, false)
                QuickActionAdapter(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("ScrollLogs", "onBindViewHolder")
        val item = items[position]
        when (holder.itemViewType) {
            TYPE_1 -> (holder as GRFeedViewHolder).bind(item)
            TYPE_2 -> (holder as GRWebinarViewHolder).bind(item)

            TYPE_3 -> (holder as ArticleAdapter).bind(item)
            TYPE_4 -> (holder as CmeAdapter).bind(item)
            TYPE_5 -> (holder as CommunityAdapter).bind(item)
            TYPE_6 -> (holder as DoctalkAdapter).bind(item)

            TYPE_7 -> (holder as EditorChoiceAdapter).bind(item)
            TYPE_8 -> (holder as EventAdapter).bind(item)
            TYPE_9 -> (holder as GameAdapter).bind(item)
            TYPE_10 -> (holder as HealthCardAdapter).bind(item)
            TYPE_11 -> (holder as HealthCardDefaultAdapter).bind(item)
            TYPE_12 -> (holder as HealthCardEditableAdapter).bind(item)
            TYPE_13 -> (holder as HealthCardFestiveAdapter).bind(item)
            TYPE_14 -> (holder as LoadingAdapter).bind(item)
            TYPE_15 -> (holder as NoStoryViewHolder).bind(item)
            TYPE_16 -> (holder as NudgeCommunityAdapter).bind(item)
            TYPE_17 -> (holder as RepostAdapter).bind(item)
            TYPE_18 -> (holder as StoryAdapter).bind(item)

            TYPE_19 -> (holder as AddToPostViewHolder).bind(item)
            TYPE_20 -> (holder as BannerAdapter).bind(item)
            TYPE_21 -> (holder as ConnectionAdapter).bind(item)
            TYPE_22 -> (holder as DrugSampleAdapter).bind(item)
            TYPE_23 -> (holder as NudgeGenericType1Adapter).bind(item)
            TYPE_24 -> (holder as NudgeGenericType2Adapter).bind(item)
            TYPE_25 -> (holder as PharmaAdapter).bind(item)
            TYPE_26 -> (holder as QuickActionAdapter).bind(item)

        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item.type) {
            1 -> TYPE_1
            2 -> TYPE_2
            3 -> TYPE_3
            4 -> TYPE_4
            5 -> TYPE_5
            6 -> TYPE_6
            7 -> TYPE_7
            8 -> TYPE_8
            9 -> TYPE_9
            10 -> TYPE_10
            11 -> TYPE_11
            12 -> TYPE_12
            13 -> TYPE_13
            14 -> TYPE_14
            15 -> TYPE_15
            16 -> TYPE_16
            17 -> TYPE_17
            18 -> TYPE_18

            19 -> TYPE_19
            20 -> TYPE_20
            21 -> TYPE_21
            22 -> TYPE_22
            23 -> TYPE_23
            24-> TYPE_24
            25 -> TYPE_25
            26 -> TYPE_26
            else -> {
                Log.e("ScrollLogs", "onBindViewHolder $item")
                throw IllegalArgumentException("Invalid item type")
            }
        }
    }
}