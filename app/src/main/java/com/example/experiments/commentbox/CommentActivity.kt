package com.example.experiments.commentbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.experiments.R
import com.example.experiments.databinding.ActivityCommentBinding

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}