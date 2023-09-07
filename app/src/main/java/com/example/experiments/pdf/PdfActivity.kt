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
        binding.pdfView.setData("/data/data/com.example.experiments/files/sample_pdf.pdf")
    }


}