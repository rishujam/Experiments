package com.example.experiments.imageeditor

import android.graphics.Matrix
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.experiments.databinding.ActivityPhotoEditBinding
import kotlin.math.roundToInt


class PhotoEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}