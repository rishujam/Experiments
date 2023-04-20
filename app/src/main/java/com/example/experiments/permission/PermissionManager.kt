package com.example.experiments.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

/*
 * Created by Sudhanshu Kumar on 14/04/23.
 */

class PermissionManager(
    val con: ComponentActivity
) {

    fun handleNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    con.applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermission by lazy {
        con.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(con, "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(con, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}