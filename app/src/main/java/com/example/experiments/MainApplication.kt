package com.example.experiments

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import io.branch.referral.Branch

class MainApplication: Application() {

    override fun onCreate() {
        Log.d("AppStartup", "app create: ${System.currentTimeMillis()}")
        super.onCreate()
        Branch.enableLogging()
        Branch.getAutoInstance(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super<DefaultLifecycleObserver>.onStart(owner)
                Log.d("LifecycleEvent", "onStart")
            }

            override fun onStop(owner: LifecycleOwner) {
                super<DefaultLifecycleObserver>.onStop(owner)
                Log.d("LifecycleEvent", "onStop")
            }
        })
    }
}