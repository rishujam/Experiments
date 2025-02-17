package com.example.experiments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.experiments.branch.BranchActivity
import com.example.experiments.databinding.ActivitySplashBinding
import io.branch.referral.Branch

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val frag = DummyFrag()
        supportFragmentManager.beginTransaction()
            .replace(R.id.flSplash, frag)
            .commit()
        binding.btnMove.setOnClickListener {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("DummyFrag", "activity on resume")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        Log.d("DummyFrag", "activity on new intent")
        val frag = DummyFrag()
        supportFragmentManager.beginTransaction()
            .replace(R.id.flSplash, frag)
            .addToBackStack(frag.toString())
            .commit()
    }
}