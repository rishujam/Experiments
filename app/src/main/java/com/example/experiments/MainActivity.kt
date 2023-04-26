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
import com.example.experiments.userstorynew.StoryActivity
import com.example.experiments.userstorynew.adapters.StoryThumbnailAdapter
import com.example.experiments.userstorynew.managers.StoryViewedStateManager
import com.example.experiments.userstorynew.models.UserList
import com.example.experiments.userstorynew.utils.StoryGen

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryThumbnailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = mainViewModel

        StoryViewedStateManager.init()

        val storyUserList = UserList(StoryGen.createData())
        Log.d("Rishu", storyUserList.list.toString())
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
    }
}