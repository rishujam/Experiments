package com.example.experiments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.experiments.camera.CameraActivity
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.imageeditor.PhotoEditActivity
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.TestFragment
import com.example.experiments.userstorynew.adapters.StoryThumbnailAdapter
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.StoryGen
import com.example.experiments.userstorynew.utils.hide
import com.example.experiments.userstorynew.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.otpless.views.OtplessManager
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryThumbnailAdapter
    private lateinit var storyUserList: UserList
    private val frag = TestFragment()
    private var infalted = false

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    private var x = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.includedSheet.reactionSheetRoot)

        binding.includedSheet.btnCloseStorySheet.setOnClickListener {
//            binding.includedSheet.apply {
//                collapsedGroup.show()
//                expandedGroup.hide()
//            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        Log.d("RishuTets", "size ${binding.includedSheet.tvTitleStorySheet.height}")
        bottomSheetBehavior.peekHeight = resources.getDimensionPixelSize(R.dimen.dimen_55dp)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.includedSheet.collapsedGroup.show()
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        binding.includedSheet.collapsedGroup.hide()
                    }
                    else -> {}
                }
            }
        })

        StoryViewedStateManager.init()
        OtplessManager.getInstance().init(this)

        storyUserList = UserList(StoryGen.getUsersWithStory())
        adapter = StoryThumbnailAdapter(storyUserList.list)
        binding.apply {
            homeRv.adapter = adapter
            homeRv.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        adapter.setOnItemClickListener { position ->
            val bundle = Bundle()
            bundle.putInt("position", position)
            bundle.putParcelable("user_list", storyUserList)
            val intent = Intent(this, StoryActivity::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)
        }

        var animShown = false
        binding.btnShowAnim.viewTreeObserver.addOnGlobalLayoutListener {
            if(!animShown) {
                animShown = true
                Log.d("AnimTest", "${binding.btnShowAnim.width}")
                showAnim()
            }
        }

        binding.btnEditor.setOnClickListener {
//            val intent = Intent(this, PhotoEditActivity::class.java)
            startActivity(Intent(this, CameraActivity::class.java))
        }

        binding.btnShowAnim.setOnClickListener {
            if(!infalted) {
                binding.viewStub.inflate()
                infalted = true
            }
        }
        setupBottomAdapter()
    }

    private fun setupBottomAdapter() {
        val reactionAdapter = StoryReactionSheetAdapter()
        binding.includedSheet.rvReactionsStory.apply {
            adapter = reactionAdapter
            layoutManager = LinearLayoutManager(context)
        }
        val list = mutableListOf(
            StoryReaction("heif", "skhf", "gdsgd","", StoryReaction.StoryReactionType.Like, false),
            StoryReaction("heif", "skhf", "gdsgd","", StoryReaction.StoryReactionType.Like, false),
            StoryReaction("heif", "skhf", "gdsgd","", StoryReaction.StoryReactionType.Like, false)
        )
        reactionAdapter.differ.submitList(list)
    }

    private fun showAnim() {
        val finalXPosition = resources.displayMetrics.widthPixels.toFloat() - 300
        val animation = TranslateAnimation(0f, finalXPosition, 0f, 0f)
        animation.duration = 1000
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Set the button's position to the final X position
                binding.btnShowAnim.x = finalXPosition
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        binding.btnShowAnim.startAnimation(animation)
    }

    fun removeCurrFrag() {
        supportFragmentManager.beginTransaction().remove(frag).commit()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        StoryViewedStateManager.destroy()
    }
}