package com.example.experiments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.pocotpless.OtplessApi
import com.example.experiments.pocotpless.ResponseData
import com.example.experiments.pocotpless.TokenRequest
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.adapters.StoryThumbnailAdapter
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.StoryGen
import com.google.gson.GsonBuilder
import com.otpless.dto.OtplessResponse
import com.otpless.views.OtplessManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryThumbnailAdapter
    private lateinit var storyUserList: UserList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = mainViewModel

        StoryViewedStateManager.init()
        OtplessManager.getInstance().init(this)

        storyUserList = UserList(StoryGen.createData())
        adapter = StoryThumbnailAdapter(storyUserList.list)
        binding.apply {
            homeRv.adapter = adapter
            homeRv.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        adapter.setOnItemClickListener {
            //TODO Pass the clicked data
            val bundle = Bundle()
            bundle.putParcelable("user_list", storyUserList)
            val intent = Intent(this, StoryActivity::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://docquity.authlink.me")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

        val api = retrofit.create(OtplessApi::class.java)


        binding.whatsappLogin.setResultCallback{
            val waId = it.waId
            val call = api.getToken("pppdicut", "uy4ab1s52isxl2jp", "application/json", TokenRequest(waId))
            call.enqueue(object : Callback<ResponseData> {
                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()
                        Log.d("RishuTest", token.toString())
                        // do something with the token
                    } else {
                        // handle error
                    }
                }
                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    Log.d("Rishutest", "error: ${t.message}")
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("RishuTest", "${StoryViewedStateManager.viewedMap}")
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        StoryViewedStateManager.destroy()
    }
}