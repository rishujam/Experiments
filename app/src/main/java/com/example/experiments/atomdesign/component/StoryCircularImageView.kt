package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.experiments.R
import com.example.experiments.databinding.StoryViewBinding

class StoryCircularImageView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : ConstraintLayout(context, attrs) {
        var binding: StoryViewBinding? = null
        var tag: String = StoryCircularImageView::class.java.simpleName

        init {
            val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = StoryViewBinding.inflate(inflater)
            binding?.let {
                addView(it.root)
            }
        }

        fun imageView() = binding?.storyProfileImage

        fun showHideRing(isViewed: Boolean) {
            if (isViewed) {
//                binding?.root?.setBackgroundResource(R.drawable.story_already_viewed_ring)
            } else {
                binding?.root?.setBackgroundResource(R.drawable.story_not_viewed_ring)
            }
        }
    }