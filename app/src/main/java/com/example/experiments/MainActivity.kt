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
import com.example.experiments.adapterCheck.CompositeAdapterV2
import com.example.experiments.data.TestData
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.imageeditor.PhotoEditActivity
import com.example.experiments.octetstream.PdfApi
import com.example.experiments.octetstream.PdfRepo
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.TestFragment
import com.example.experiments.userstorynew.adapters.StoryThumbnailAdapter
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.StoryGen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Dispatchers.IO)

    private var x = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("AppStartup", "Main Activity onCreate: ${System.currentTimeMillis()}")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter1 = CompositeAdapterV2(this)
        binding.rv.apply {
            adapter = adapter1
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        scope.launch {
            delay(5000L)
            Log.d("AppStartup", "delay finished")
        }
//        scope.cancel()

        adapter1.submitList(createDummyData())



//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
//        val api = Retrofit.Builder()
//            .baseUrl("https://fs-dev.dxassist.ai/")
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(PdfApi::class.java)
//        val repo = PdfRepo(api)
//
//        var animShown = false
    }

    private fun createDummyData(): List<TestData> {
        val list = mutableListOf<TestData>()
        for (i in 1 until 120){
            val type = (1..26).random()
            list.add(TestData(type))
        }

        return list
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Lifecycle", "onRestart main Activity")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "onStart main activity")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "onStop main activity")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume Main Activity")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause mainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy mainActivity")
    }
}