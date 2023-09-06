package com.example.experiments

import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

/*
 * Created by Sudhanshu Kumar on 03/07/23.
 */

interface TestApi {

    @GET
    fun get(@Url url: String): Call<Any>

    companion object {
        private val client = OkHttpClient.Builder().apply {
            addInterceptor(MagistrateInterceptor())
        }.build()
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://mocki.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val api: TestApi by lazy {
            retrofit.create(TestApi::class.java)
        }
    }

}