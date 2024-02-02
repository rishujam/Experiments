package com.example.experiments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
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
import kotlinx.coroutines.launch
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
        }

        binding.btnShowAnim.setOnClickListener {
            if(!infalted) {
                binding.viewStub.inflate()
                infalted = true
            }
        }

        binding.btn2.setOnClickListener {
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
            lifecycleScope.launch(Dispatchers.IO) {
                repo.getPdf(
                    "https://fs-dev.dxassist.ai/01HDDEDDWSPK2GFTHJSYEFRNDT?Expires=1709380969&Signature=a97XLSN7CChhDInxvSuwaAeAHDWi-51i3qwKKkdqh9yCqoKrypVNL98PV0MucSrWr5oPukQDkEo-6I6x2eA3VpEauX6G-fcGsU6~qbtPugrmfA3wC-aGa42FT6mmHXpK4KylYYveFlJAXj4cecO2F-RbssrchNg01VB6Py7h5Lz-UQhjgnXEw1ZQ1frqbiEs5NiEnovYh4IczxCCUdrzi~ijbNssoShbfz3dXCc8HJLWB~MSsvj4LxdQvtCOpnvVjZZ8WIA0OyHRPY9gsE4Ywb0h5eXB6FN0LSewBe95z4ZnmCFDla0VloCBrjMVyTolZHv5BGMFw68teneXBBHzJw__&Key-Pair-Id=K2BC7R8SEVH8JU"
                )
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