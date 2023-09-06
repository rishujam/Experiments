package com.example.experiments

import android.util.Log
import okhttp3.Interceptor

/*
 * Created by Sudhanshu Kumar on 28/06/23.
 */

class MagistrateInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        val request = chain.request().newBuilder()
            .build()

        Log.d("RishuTest", "url: ${request.url}")

        return chain.proceed(request)

    }
}