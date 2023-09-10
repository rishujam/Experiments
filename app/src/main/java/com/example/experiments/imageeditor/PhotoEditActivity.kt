package com.example.experiments.imageeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.experiments.R
import com.example.experiments.databinding.ActivityPhotoEditBinding

class PhotoEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}