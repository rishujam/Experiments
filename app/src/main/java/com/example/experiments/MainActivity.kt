package com.example.experiments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.pocotpless.OtplessApi
import com.example.experiments.pocotpless.ResponseData
import com.example.experiments.pocotpless.TokenRequest
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.TestFragment
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
    private val frag = TestFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = mainViewModel

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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://docquity.authlink.me")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

        val api = retrofit.create(OtplessApi::class.java)

        binding.button.setOnClickListener {
            binding.fm.visibility = View.VISIBLE

            setCurrentFragment(frag)
        }

        TooltipCompat.setTooltipText(binding.button, "This is a tooltip")

        binding.button.setOnClickListener {

        }






//        binding.whatsappLogin.setResultCallback{
//            val waId = it.waId
//            val call = api.getToken("pppdicut", "uy4ab1s52isxl2jp", "application/json", TokenRequest(waId))
//            call.enqueue(object : Callback<ResponseData> {
//                override fun onResponse(
//                    call: Call<ResponseData>,
//                    response: Response<ResponseData>
//                ) {
//                    if (response.isSuccessful) {
//                        val token = response.body()
//                        // do something with the token
//                    } else {
//                        // handle error
//                    }
//                }
//                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
//
//                }
//            })
//        }
    }

    private fun showTooltip(view: View) {
        TooltipCompat.setTooltipText(view, null) // Clear the tooltip text temporarily
        ViewCompat.postOnAnimationDelayed(view, {
            TooltipCompat.setTooltipText(view, "")
        }, 100) // Delay before showing the tooltip (adjust as needed)
    }

    fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fm,fragment)
            commit()
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