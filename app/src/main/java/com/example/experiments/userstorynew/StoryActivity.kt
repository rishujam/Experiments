package com.example.experiments.userstorynew

import android.animation.Animator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseIntArray
import android.widget.Toast
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.experiments.databinding.ActivityStoryBinding
import com.example.experiments.userstorynew.adapters.StoryPagerAdapter
import com.example.experiments.userstorynew.listeners.AutoNavigateListener
import com.example.experiments.userstorynew.listeners.StoryPageChangeListener
import com.example.experiments.userstorynew.models.UserData
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.CubeOutTransformer
import com.example.experiments.userstorynew.utils.StoryGen

class StoryActivity : AppCompatActivity(), AutoNavigateListener {

    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras?.getParcelable<UserList>("user_list")
        setUpPager((data as UserList).list)
    }

    override fun backPageNavigate() {
        if (binding.viewPager.currentItem > 0) {
            try {
                fakeDrag(false)
            } catch (e: Exception) {
                //NO OP
            }
        }
    }

    override fun nextPageNavigate() {
        if (binding.viewPager.currentItem + 1 < (binding.viewPager.adapter?.count ?: 0)) {
            try {
                fakeDrag(true)
            } catch (e: Exception) {
                //NO OP
            }
        } else {
            //there is no next story
            Toast.makeText(this, "All stories displayed.", Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var pagerAdapter: StoryPagerAdapter
    private var currentPage: Int = 0

    private fun setUpPager(storyUserList: ArrayList<UserData>) {
        pagerAdapter = StoryPagerAdapter(
            supportFragmentManager,
            storyUserList
        )
        binding.viewPager.apply {
            adapter = pagerAdapter
            currentItem = currentPage
            setPageTransformer(
                true,
                CubeOutTransformer()
            )
        }
        binding.viewPager.addOnPageChangeListener(object : StoryPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }

            override fun onPageScrollCanceled() {
                currentFragment().resumeCurrentStory()
            }
        })
    }

    private fun currentFragment(): StoryFragment {
        return pagerAdapter.findFragmentByPosition(binding.viewPager, currentPage) as StoryFragment
    }

    private var prevDragPosition = 0

    private fun fakeDrag(forward: Boolean) {
        if (prevDragPosition == 0 && binding.viewPager.beginFakeDrag()) {
            ValueAnimator.ofInt(0, binding.viewPager.width).apply {
                duration = 400L
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

    companion object {
        val progressState = SparseIntArray()
    }
}