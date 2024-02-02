package com.example.experiments.octetstream

import android.util.Log
import com.example.experiments.pocotpless.ResponseData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.ResponseBody
import retrofit2.Response
import okio.ByteString.Companion.toByteString

/*
 * Created by Sudhanshu Kumar on 02/02/24.
 */

class PdfRepo(
    private val api: PdfApi
) {

    val listBytes = mutableListOf<ByteArray>()
    val range1 = "bytes=0-1000000"
    val range2 = "bytes=1000001-19658049"
    val range3 = "bytes=19658049-19658099"

    suspend fun getPdf(url: String) {
        coroutineScope {
            val deferredReq = mutableListOf<Deferred<Response<ResponseBody>>>()
            val res = async { api.getFile(url, range1) }
            val res2 = async { api.getFile(url, range2) }
            val res3 = async { api.getFile(url, range3) }
            deferredReq.add(res)
            deferredReq.add(res2)
            deferredReq.add(res3)
            val reqList = deferredReq.awaitAll()
            for(i in reqList.indices) {
                val req = reqList[i]
                if(req.isSuccessful) {
                    Log.d("RishuTest", "is Success $i")
                    req.body()?.bytes()?.let { listBytes.add(it) }
                } else {
                    Log.d("RishuTest", "is error $i")
                }
            }
            Log.d("RishuTest", "array1: ${listBytes[0].toByteString()}")
            Log.d("RishuTest", "array2: ${listBytes[1].toByteString()}")
            Log.d("RishuTest", "array3: ${listBytes.getOrNull(2)?.toByteString()}")
        }

    }

}