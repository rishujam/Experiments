package com.example.experiments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.experiments.branch.BranchActivity
import io.branch.referral.Branch

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback { branchUniversalObject, linkProperties, error ->
            if (error != null) {
                Log.e(TAG, "branch init failed. Caused by -" + error.message)
            } else {
                Log.i(TAG, "branch init complete!")
                if (branchUniversalObject != null) {
                    Log.i(TAG, "title " + branchUniversalObject.title)
                    Log.i(TAG, "CanonicalIdentifier " + branchUniversalObject.canonicalIdentifier)
                    Log.i(TAG, "metadata " + branchUniversalObject.contentMetadata.convertToJson())
                }
                if (linkProperties != null) {
                    Log.i(TAG, "Channel " + linkProperties.channel)
                    Log.i(TAG, "control params " + linkProperties.controlParams)
                    Log.i(
                        TAG,
                        linkProperties.controlParams["\$og_title"].toString()
                    )
                    // ---------- Intra App Linking Using Custom $deeplink_path ----------
                    // ---------- Intra-app linking (i.e. session reinitialization) requires an intent flag, "branch_force_new_session". ----------
                    if (linkProperties.controlParams["\$deeplink_path"].toString() == "color block page") {
//                        val intent = Intent(this, ColorBlockPage::class.java)
//                        intent.putExtra("branch_force_new_session", true)
//                        startActivity(intent)
                    }
                    else if (linkProperties.controlParams["\$deeplink_path"].toString() == "read deep link page") {
                        val intent = Intent(this, BranchActivity::class.java)
                        intent.putExtra("branch_force_new_session", true)
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }.withData(this.intent.data).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        Branch.sessionBuilder(this).withCallback { referringParams, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", error.message)
            } else if (referringParams != null) {
                Log.i("BranchSDK_Tester", referringParams.toString())
            }
        }.reInit()
    }
}