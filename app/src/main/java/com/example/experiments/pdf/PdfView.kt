package com.example.experiments.pdf

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.experiments.databinding.PdfViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/*
 * Created by Sudhanshu Kumar on 06/09/23.
 */

class PdfView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: PdfViewBinding
    private lateinit var pdfAdapter: PDFAdapter
    private var noOfPages: Int? = null
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        binding = PdfViewBinding.inflate(LayoutInflater.from(context))
        removeAllViews()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        pdfAdapter = PDFAdapter()
        binding.pdfRv.apply {
            adapter = pdfAdapter
            layoutManager = LinearLayoutManager(context)
            setOnScrollChangeListener { _, _, _, _, _ ->
                val currPage = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1
                noOfPages?.let {
                    binding.tvPageNo.text = "$currPage / $it"
                    showPagesViewForSomeSeconds(coroutineScope, context)
                }
            }
        }
    }

    fun setData(list: List<PdfUiModel>) {
        pdfAdapter.differ.submitList(list)
        noOfPages = list.size
        showPagesViewForSomeSeconds(coroutineScope, context)
    }

}