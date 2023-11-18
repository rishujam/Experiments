package com.example.experiments

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/*
 * Created by Sudhanshu Kumar on 06/10/23.
 */

class SampleClass(
    private val activity: Activity
) {

    private var var1 = 1

    init {
//        val receiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                Log.d("RishuTest", "var1: $var1 obj: ${this@SampleClass}")
//            }
//        }
//        activity.registerReceiver(
//            receiver,
//            IntentFilter("broadcastTest")
//        )
        TimberExt.testI = object : TestI {
            override fun onReceiver() {
                Log.d("RishuTest", "car1: $var1")
            }
        }
    }

}