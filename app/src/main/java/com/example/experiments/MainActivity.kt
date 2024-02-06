package com.example.experiments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.octetstream.PdfApi
import com.example.experiments.octetstream.PdfRepo
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.TestFragment
import com.example.experiments.userstorynew.adapters.StoryThumbnailAdapter
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.StoryGen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

    private var x = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyUserList = UserList(StoryGen.getUsersWithStory())
        adapter = StoryThumbnailAdapter(storyUserList.list)
        binding.apply {
            homeRv.adapter = adapter
            homeRv.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
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

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val api = Retrofit.Builder()
            .baseUrl("https://fs-dev.dxassist.ai/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PdfApi::class.java)
        val repo = PdfRepo(api)

        var animShown = false
        binding.btnShowAnim.viewTreeObserver.addOnGlobalLayoutListener {
            if (!animShown) {
                animShown = true
                Log.d("AnimTest", "${binding.btnShowAnim.width}")
                showAnim()
            }
        }

        binding.btnShowAnim.setOnClickListener {
            if (!infalted) {
                binding.viewStub.inflate()
                infalted = true
            }
        }
        val url =
            "https://fs-dev.dxassist.ai/01HGTSHH879QACA5374CX07WNE?Expires=1709805634&Signature=QjnWDiFdOqHfFtqv0L7eokhYeawkxVmnzasdJgz--Gw8Sc4R6gWmws63CmWqug-TRt8zE5Pjinx9wenbCmawA7stWONiqoACyQIWU63jWSF4WaWTZlglQDUD~wxJ75rjMDRK2gk~aq9kotkeP4Gyo5PWj0n4NuGzmfW~IX3p3qQnJeLUFAd-WIJ10pFcXIaX9WVhkmxUZ~bcfbu0SjxlLCTGvsau7jxCz~GOpVs-HmDO~kWHjSjfQAaVdx5y~tXX4BKyUXHfNwn2DigqL~-tG29Ppj7RyGM-4kt7YwG2Cmi2SQ0h5DErXugziBkfO12LEAlm1XYkCf4OiWgQxjtZtw__&Key-Pair-Id=K2BC7R8SEVH8JU"

        binding.btn2.setOnClickListener {
            var firstTime = false
            val t1 = System.currentTimeMillis()
            lifecycleScope.launch(Dispatchers.IO) {
                val uri = repo.downloadPdf(url, applicationContext)
                uri?.let {
                    repo.render(it.toString()).collect {
                        if (!firstTime) {
                            Log.d("RishuTest", "first emit: ${System.currentTimeMillis() - t1}")
                            firstTime = true
                        }
                    }
                }
            }
        }
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