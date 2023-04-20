package com.example.experiments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
 * Created by Sudhanshu Kumar on 02/04/23.
 */

class MainViewModel : ViewModel() {

    fun m() = viewModelScope.launch(Dispatchers.IO) {
        val oA = async { a() }
        val oB = async { b() }
        val oC = async { c() }

        val data1 = oA.await()
        Log.d("RishuTest", "data1 assigned with: $data1")
        val data2 = oB.await()
        Log.d("RishuTest", "data2 assigned with: $data2")
        val data3 = oC.await()
        Log.d("RishuTest", "data3 assigned with: $data3")
    }

    fun v() = viewModelScope.launch(Dispatchers.IO) {
        val oA = async { a() }.await()
        Log.d("RishuTest", "data1 assigned with: $oA")
        val oB = async { b() }.await()
        Log.d("RishuTest", "data2 assigned with: $oB")
        val oC = async { c() }.await()
        Log.d("RishuTest", "data3 assigned with: $oC")


    }

    suspend fun a(): String{
        //Log.d("RishuTest", "a completed")
        return "data1"
    }

    suspend fun b(): String {
        delay(5000)
        //Log.d("RishuTest", "b completed")
        return "data2"
    }

    suspend fun c(): String{
        delay(7000)
        //Log.d("RishuTest", "c completed")
        return "data3"
    }

    fun clicked() {
        Log.d("RishuTest", "clicked")
    }
}