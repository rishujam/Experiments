package com.example.experiments.octetstream

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Streaming
import retrofit2.http.Url


/*
 * Created by Sudhanshu Kumar on 02/02/24.
 */

interface PdfApi {

    @GET
    @Headers("Content-Type:application/octet-stream")
    suspend fun getFileBytes(
        @Url fileUrl: String,
        @Header("Range") byteRange: String
    ): Response<ResponseBody>

    @GET
    suspend fun getFile(
        @Url fileUrl: String
    ): Response<ResponseBody>

}