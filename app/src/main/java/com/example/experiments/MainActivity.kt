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
import com.example.experiments.imageeditor.PhotoEditActivity
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.TestFragment
import com.example.experiments.userstorynew.adapters.StoryThumbnailAdapter
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.StoryGen
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

        binding.btnEditor.setOnClickListener {
            startActivity(Intent(this, PhotoEditActivity::class.java))
        }

        binding.btnShowAnim.setOnClickListener {
            createBranchLink()
        }

    }

    val buo = BranchUniversalObject()
        .setTitle("Title Link")
        .setContentDescription("Link created using the Branch SDK")

    private fun createBranchLink() {
        val lp = LinkProperties()
            .setCampaign("Sample Test App Campaign Example")
            .setChannel("Sample Test App Marketing")
            .setFeature("sharing")
            .addControlParameter("\$desktop_url", "https://example.com/home")
            .addControlParameter("\$deeplink_path", "color block page")
            .addControlParameter("blockColor", "Yellow")

        buo.generateShortUrl(this, lp, Branch.BranchLinkCreateListener { url, error ->
            if (error == null) {
                Log.i(TAG, "Branch Link to share: $url")
                binding.tvBranchLink.text = url.toString()
            }
        })
    }

    private fun shareBranchLink() {
        val lp = LinkProperties()
            .setChannel("facebook")
            .setFeature("sharing")
            .setCampaign("content 123 launch")
            .setStage("new user")

            // $deeplink_path routes users to a specific Activity. Uncomment one of the below to route to the specified page.
            .addControlParameter("\$deeplink_path", "color block page")
            //.addControlParameter("\$deeplink_path", "read deep link page")


            // You can set the 'blockColor' parameter to 'Blue', 'Yellow', 'Red', 'Green' or 'White' to modify the color block page.
            .addControlParameter("blockColor", "Green")

            .addControlParameter("\$desktop_url", "https://example.com/home")
            .addControlParameter("custom", "data")
            .addControlParameter("custom_random", Calendar.getInstance().timeInMillis.toString())

        val ss = ShareSheetStyle(this@MainActivity, "Check this out!", "This stuff is awesome: ")
            .setCopyUrlStyle(resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_menu_copy_mtrl_am_alpha), "Copy", "Added to clipboard")
            .setMoreOptionStyle(resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_search_api_material), "Show more")
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
            .setAsFullWidthStyle(true)
            .setSharingTitle("Share With")

        buo.showShareSheet(this, lp, ss, object : Branch.BranchLinkShareListener {
            override fun onShareLinkDialogLaunched() {}
            override fun onShareLinkDialogDismissed() {}
            override fun onLinkShareResponse(sharedLink: String?, sharedChannel: String?, error: BranchError?) {}
            override fun onChannelSelected(channelName: String) {}
        })
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