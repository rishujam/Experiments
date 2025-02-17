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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.wait
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
            Log.d(
                "RishuTest",
                "length: $length time in calc length: ${System.currentTimeMillis() - t1}"
            )
            if (length > MIN_SIZE_BYTES_DOWNLOAD) {
                Log.d("RishuTest", "enter MIN_SIZE_BYTES_DOWNLOAD")
                val noOfChunks = if (length % CHUNK_SIZE == 0L) {
                    length / CHUNK_SIZE
                } else {
                    (length / CHUNK_SIZE) + 1
                }
                var startByte = 0L
                var endByte = CHUNK_SIZE
                val deferredReqList = mutableListOf<Deferred<ByteArray?>>()
                val t2 = System.currentTimeMillis()
                for (i in 1..noOfChunks) {
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

    private fun high(fu: (Int, Int, Int) -> Unit) {
        fu(1,1,1)

    }

    private fun getLength(urlStr: String): Long {
        high { i, i2, i3 ->

        }
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
        return if (res.isSuccessful) {
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

    fun loop() {
        val t1 = System.currentTimeMillis()
        for(i in 0..1000000000) { }
        Log.d("RishuTest", "${System.currentTimeMillis() - t1}")
    }

    suspend fun test() {
        coroutineScope {
            val list = mutableListOf<Deferred<Unit>>()
            val a1 = async { loop() }
            val a2 = async { loop() }
            val a3 = async { loop() }
            val a4 = async { loop() }
            val a5 = async { loop() }
            val a6 = async { loop() }
            val a7 = async { loop() }
            val a8 = async { loop() }
            val a9 = async { loop() }
            val a10 = async { loop() }
            val a11 = async { loop() }
            val a12 = async { loop() }
            val a13 = async { loop() }
            val a14 = async { loop() }
            val a15 = async { loop() }
            val a16 = async { loop() }
            val a17 = async { loop() }
            val a18 = async { loop() }
            val a19 = async { loop() }
            val a20 = async { loop() }
            val a21 = async { loop() }
            val a22 = async { loop() }
            val a23 = async { loop() }
            val a24 = async { loop() }
            val a25 = async { loop() }
            val a26 = async { loop() }
            val a27 = async { loop() }
            val a28 = async { loop() }
            val a29 = async { loop() }
            val a30 = async { loop() }
            val a31 = async { loop() }
            val a32 = async { loop() }
            val a33 = async { loop() }
            val a34 = async { loop() }
            val a35 = async { loop() }
            val a36 = async { loop() }
            val a37 = async { loop() }
            val a38 = async { loop() }
            val a39 = async { loop() }
            val a40 = async { loop() }
            val a41 = async { loop() }
            val a42 = async { loop() }
            val a43 = async { loop() }
            val a44 = async { loop() }
            val a45 = async { loop() }
            val a46 = async { loop() }
            val a47 = async { loop() }
            val a48 = async { loop() }
            val a49 = async { loop() }
            val a50 = async { loop() }
            val a51 = async { loop() }
            val a52 = async { loop() }
            val a53 = async { loop() }
            val a54 = async { loop() }
            val a55 = async { loop() }
            val a56 = async { loop() }
            val a57 = async { loop() }
            val a58 = async { loop() }
            val a59 = async { loop() }
            val a60 = async { loop() }
            val a61 = async { loop() }
            val a62 = async { loop() }
            val a63 = async { loop() }
            val a64 = async { loop() }
            val a65 = async { loop() }
            val a66 = async { loop() }
            val a67 = async { loop() }
            val a68 = async { loop() }
            val a69 = async { loop() }
            val a70 = async { loop() }
            val a71 = async { loop() }
            val a72 = async { loop() }
            val a73 = async { loop() }
            val a74 = async { loop() }
            val a75 = async { loop() }
            val a76 = async { loop() }
            val a77 = async { loop() }
            val a78 = async { loop() }
            val a79 = async { loop() }
            val a80 = async { loop() }
            val a81 = async { loop() }
            list.addAll(
                listOf(
                    a1,
                    a2,
                    a3,
                    a4,
                    a5,
                    a6,
                    a7,
                    a8,
                    a9,
                    a10,
                    a11,
                    a12,
                    a13,
                    a14,
                    a15,
                    a16,
                    a17,
                    a18,
                    a19,
                    a20,
                    a21,
                    a22,
                    a23,
                    a24,
                    a25,
                    a26,
                    a27,
                    a28,
                    a29,
                    a30,
                    a31,
                    a32,
                    a33,
                    a34,
                    a35,
                    a36,
                    a37,
                    a38,
                    a39,
                    a40,
                    a41,
                    a42,
                    a43,
                    a44,
                    a45,
                    a46,
                    a47,
                    a48,
                    a49,
                    a50,
                    a51,
                    a52,
                    a53,
                    a54,
                    a55,
                    a56,
                    a57,
                    a58,
                    a59,
                    a60,
                    a61,
                    a62,
                    a63,
                    a64,
                    a65,
                    a66,
                    a67,
                    a68,
                    a69,
                    a70,
                    a71,
                    a72,
                    a73,
                    a74,
                    a75,
                    a76,
                    a77,
                    a78,
                    a79,
                    a80,
                    a81
                )
            )
            val t1 = System.currentTimeMillis()
            list.awaitAll()
            Log.d("RishuTest", "time : ${System.currentTimeMillis() - t1}")
        }
    }
}

data class PdfUiModel(
    val bitmap: Bitmap,
    val pageNo: Int,
    val totalPages: Int
)
