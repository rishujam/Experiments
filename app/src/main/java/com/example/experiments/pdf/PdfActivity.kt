package com.example.experiments.pdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.experiments.R
import com.example.experiments.databinding.ActivityPdfBinding

class PdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = mutableListOf<PdfUiModel>()
        val d1 = ContextCompat.getDrawable(this, R.drawable.sample)?.toBitmap()
        val d2 = ContextCompat.getDrawable(this, R.drawable.sample)?.toBitmap()
        if(d1 != null && d2 != null) {
            data.add(PdfUiModel(d1, 1, 2))
            data.add(PdfUiModel(d2, 2, 2))
            binding.pdfView.setData(data)
        }
    }


}