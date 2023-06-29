package com.example.experiments.userstorynew

import android.animation.Animator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.experiments.databinding.ActivityStoryBinding
import com.example.experiments.userstorynew.adapters.StoryDetailPagerAdapter
import com.example.experiments.userstorynew.listeners.AutoNavigateListener
import com.example.experiments.userstorynew.listeners.StoryPageChangeListener
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserData
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.CubeOutTransformer

class StoryActivity : AppCompatActivity(), AutoNavigateListener {

    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.let {
            val data = it.getParcelable<UserList>("user_list")
            val position = it.getInt("position")
            setUpPager((data as UserList).list, position)
        }
    }

    override fun backPageNavigate() {
        if (binding.viewPager.currentItem > 0) {
            try {
                fakeDrag(false)
            } catch (e: Exception) {
                //NO OP
            }
        } else {
            finish()
        }
    }

    override fun nextPageNavigate() {
        if (binding.viewPager.currentItem + 1 < (binding.viewPager.adapter?.itemCount ?: 0)) {
            try {
                fakeDrag(true)
            } catch (_: Exception) { }
        } else {
            finish()
        }
    }

    private lateinit var pagerAdapter: StoryDetailPagerAdapter
    private var currentPage: Int = 0

    private fun setUpPager(storyUserList: ArrayList<UserData>, position: Int) {
        pagerAdapter = StoryDetailPagerAdapter(
            this,
            storyUserList
        )
        binding.viewPager.apply {
            adapter = pagerAdapter
            setCurrentItem(position, false)
            currentPage = position
        }
        binding.viewPager.registerOnPageChangeCallback(object : StoryPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
            override fun onPageScrollCanceled() {
                currentFragment()?.playStory()
            }
        })
    }

    private fun currentFragment(): StoryFragment? {
        return pagerAdapter.findFragmentByPosition(currentPage) as? StoryFragment
    }

    private var prevDragPosition = 0

    private fun fakeDrag(forward: Boolean) {
        if (prevDragPosition == 0 && binding.viewPager.beginFakeDrag()) {
            ValueAnimator.ofInt(0, binding.viewPager.width).apply {
                duration = CUBE_ANIM_TIME
                interpolator = FastOutSlowInInterpolator()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {  }

                    override fun onAnimationEnd(animation: Animator) {
                        removeAllUpdateListeners()
                        if (binding.viewPager.isFakeDragging) {
                            binding.viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        removeAllUpdateListeners()
                        if (binding.viewPager.isFakeDragging) {
                            binding.viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationRepeat(animation: Animator) {  }
                })
                addUpdateListener {
                    if (!binding.viewPager.isFakeDragging) return@addUpdateListener
                    val dragPosition: Int = it.animatedValue as Int
                    val dragOffset: Float =
                        ((dragPosition - prevDragPosition) * if (forward) -1 else 1).toFloat()
                    prevDragPosition = dragPosition
                    binding.viewPager.fakeDragBy(dragOffset)
                }
            }.start()
        }
    }

    fun setPageTransformAnimation() {
        binding.viewPager.setPageTransformer(CubeOutTransformer())
    }

    override fun onStop() {
        super.onStop()
        binding.viewPager.setPageTransformer(null)
    }

    override fun onResume() {
        super.onResume()
        Log.d("RishuTest", "${StoryViewedStateManager.viewedMap}")
    }

    override fun onDestroy() {
        super.onDestroy()
        pagerAdapter.clearFragments()
    }

    companion object {
        private const val CUBE_ANIM_TIME = 400L
    }
}