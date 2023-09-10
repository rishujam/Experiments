package com.example.experiments

import android.content.Intent
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
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.TestFragment
import com.example.experiments.userstorynew.adapters.StoryThumbnailAdapter
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.StoryGen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.otpless.views.OtplessManager


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryThumbnailAdapter
    private lateinit var storyUserList: UserList
    private val frag = TestFragment()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.includedSheet.bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset:Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> Toast.makeText(this@MainActivity, "STATE_COLLAPSED", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_EXPANDED -> Toast.makeText(this@MainActivity, "STATE_EXPANDED", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_DRAGGING -> Toast.makeText(this@MainActivity, "STATE_DRAGGING", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_SETTLING -> Toast.makeText(this@MainActivity, "STATE_SETTLING", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_HIDDEN -> Toast.makeText(this@MainActivity, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this@MainActivity, "OTHER_STATE", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.btnBottomSheetPersistent.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

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

    }

//    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            mScaleFactor *= scaleGesture.scaleFactor
//            binding.zoomTest.scaleX = mScaleFactor
//            binding.zoomTest.scaleY = mScaleFactor
//            return true
//        }
//
//        override fun onScaleEnd(detector: ScaleGestureDetector) {
//            super.onScaleEnd(detector)
//            mScaleFactor = 1.0f
//            binding.zoomTest.scaleX = mScaleFactor
//            binding.zoomTest.scaleY = mScaleFactor
//        }
//    }

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