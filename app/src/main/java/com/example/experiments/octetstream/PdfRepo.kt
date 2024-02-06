package com.example.experiments.octetstream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.net.URL


/*
 * Created by Sudhanshu Kumar on 02/02/24.
 */

class PdfRepo(
    private val api: PdfApi
) {

    companion object {
        private const val CHUNK_SIZE = 2000000L
        private const val MIN_SIZE_BYTES_DOWNLOAD = 10000000L
    }

    suspend fun downloadPdf(url: String, context: Context): URI? {
        var uri: URI? = null
        coroutineScope {
            val t1 = System.currentTimeMillis()
            val length = getLength(url)
            Log.d("RishuTest", "time in calc length: ${System.currentTimeMillis() - t1}")
            if (length > MIN_SIZE_BYTES_DOWNLOAD) {
                Log.d("RishuTest", "enter MIN_SIZE_BYTES_DOWNLOAD")
                val noOfChunks = if(length % CHUNK_SIZE == 0L) {
                    length / CHUNK_SIZE
                } else {
                    (length / CHUNK_SIZE) + 1
                }
                var startByte = 0L
                var endByte = CHUNK_SIZE
                val deferredReqList = mutableListOf<Deferred<ByteArray?>>()
                val t2 = System.currentTimeMillis()
                for(i in 1..noOfChunks) {
                    val range = "bytes=$startByte-$endByte"
                    val deferredReq = async { getPdfBytes(url, range) }
                    deferredReqList.add(deferredReq)
                    startByte = endByte + 1L
                    endByte += CHUNK_SIZE
                }
                Log.d("RishuTest", "time after adding req: ${System.currentTimeMillis() - t2}")
                val t3 = System.currentTimeMillis()
                val results = deferredReqList.awaitAll()
                Log.d("RishuTest", "time after waiting: ${System.currentTimeMillis() - t3}")
                val t4 = System.currentTimeMillis()
                uri = saveByteArraysToFile(context, results)
                Log.d("RishuTest", "time after saving in local: ${System.currentTimeMillis() - t4}")
            } else {
                val pdfBytes = getPdf(url)
                val filePath = saveFileToInternalStorage(context, pdfBytes)
                uri = filePath
            }
        }
        return uri
    }

    private fun getLength(urlStr: String): Long {
        val url = URL(urlStr)
        val urlConnection = url.openConnection()
        urlConnection.connect()
        return urlConnection.contentLength.toLong()
    }

    private suspend fun getPdf(url: String): ByteArray? {
        val res = api.getFile(url)
        return if (res.isSuccessful) {
            res.body()?.bytes()
        } else {
            null
        }
    }

    private suspend fun getPdfBytes(url: String, range: String): ByteArray? {
        val res = api.getFileBytes(url, range)
        return if(res.isSuccessful) {
            res.body()?.bytes()
        } else {
            null
        }
    }

    private fun saveFileToInternalStorage(
        context: Context,
        bytes: ByteArray?
    ): URI? {
        return bytes?.let {
            try {
                val path = context.getExternalFilesDir(null)
                val folder = File(path, "pdfs")
                folder.mkdirs()
                val file = File(folder, "output.pdf")
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(bytes)
                }
                file.toURI()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun saveByteArraysToFile(
        context: Context,
        listBytes: List<ByteArray?>
    ): URI? {
        val path = context.getExternalFilesDir(null)
        val folder = File(path, "pdfs")
        folder.mkdirs()
        val file = File(folder, "output.pdf")
        FileOutputStream(file).use { outputStream ->
            for (byteArray in listBytes) {
                byteArray?.let {
                    try {
                        outputStream.write(byteArray)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return file.toURI()
    }

    fun render(uri: String): Flow<List<PdfUiModel>> = flow {
        try {
            val output = mutableListOf<PdfUiModel>()
            val url = URI(uri)
            val input = ParcelFileDescriptor.open(File(url), ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(input)
            val pageCount = renderer.pageCount
            for (i in 0 until pageCount) {
                val page = renderer.openPage(i)
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                val model = PdfUiModel(bitmap, i + 1, pageCount)
                output.add(model)
                if (output.size >= 2) {
                    emit(output)
                    output.clear()
                }
                page.close()
            }
            if (output.isNotEmpty()) {
                emit(output)
            }
            renderer.close()
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}

data class PdfUiModel(
    val bitmap: Bitmap,
    val pageNo: Int,
    val totalPages: Int
)
