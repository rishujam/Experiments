package com.example.experiments.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.experiments.R
import com.example.experiments.databinding.PdfItemViewBinding
import com.example.experiments.databinding.PdfViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.net.URI

/*
 * Created by Sudhanshu Kumar on 06/09/23.
 */

class PdfView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var pdfAdapter: PDFAdapter
    private var noOfPages: String? = null
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var pageText: TextView
    private var pageCard: CardView

    init {
        removeAllViews()
        LayoutInflater.from(context).inflate(R.layout.pdf_view, this, true)
        pageText = findViewById(R.id.tvPageNo)
        pageCard = findViewById(R.id.cdPages)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        pdfAdapter = PDFAdapter()
        val rv = findViewById<RecyclerView>(R.id.pdfRv)
        rv.apply {
            adapter = pdfAdapter
            layoutManager = LinearLayoutManager(context)
            setOnScrollChangeListener { _, _, _, _, _ ->
                val visiblePosition = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if(visiblePosition != -1) {
                    val currPage = visiblePosition + 1
                    if(!noOfPages.isNullOrEmpty()) {
                        pageCard.show()
                        val text = "$currPage / $noOfPages"
                        pageText.text = text
                        pageCard.showPagesViewForSomeSeconds(coroutineScope, context)
                    }
                }

            }
        }
    }

    fun setData(uri: String) {
        val list = generateBitmaps(uri)
        noOfPages = list.size.toString()
        pdfAdapter.differ.submitList(list)
        pageCard.show()
        pageCard.showPagesViewForSomeSeconds(coroutineScope, context)
    }

    private fun generateBitmaps(uri: String): List<PdfUiModel> {
        val output = mutableListOf<PdfUiModel>()
        val input = ParcelFileDescriptor.open(File(uri), ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(input)
        val pageCount = renderer.pageCount
        for(i in 0 until pageCount) {
            val page = renderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            val model = PdfUiModel(bitmap, i + 1, pageCount)
            output.add(model)
            page.close()
        }
        renderer.close()
        return output
    }

}