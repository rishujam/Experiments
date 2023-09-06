package com.example.experiments.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import java.io.File
import java.net.URI

/*
 * Created by Sudhanshu Kumar on 04/09/23.
 */

object PdfUtil {

    fun render(uri: String): List<PdfUiModel> {
        val output = mutableListOf<PdfUiModel>()
        val url = URI(uri)
        val input = ParcelFileDescriptor.open(File(url), ParcelFileDescriptor.MODE_READ_ONLY)
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