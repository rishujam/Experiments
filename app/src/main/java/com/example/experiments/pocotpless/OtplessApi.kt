package com.example.experiments.pocotpless

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/*
 * Created by Sudhanshu Kumar on 26/04/23.
 */

interface OtplessApi {

    @POST("/")
    fun getToken(
        @Header("clientId") clientId: String,
        @Header("clientSecret") clientSecret: String,
        @Header("Content-Type") contentType: String,
        @Body request: TokenRequest
    ): Call<ResponseData>
}