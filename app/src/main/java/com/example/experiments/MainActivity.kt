package com.example.experiments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.permission.PermissionManager
import com.example.experiments.userstory.UserStoryActivity
import com.example.logmate.LogMate
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = mainViewModel
        binding.button.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, UserStoryActivity::class.java))
        }

        LogMate.d("main activity: 0")


    }

    private fun readFile() {
        val fis: FileInputStream = openFileInput("20FebPm.txt")
        val isr = InputStreamReader(fis)
        val bufferedReader = BufferedReader(isr)
        val sb = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        Log.d("RishuTest", "string: $sb")
    }

    private fun createFileName(): kotlin.String {
        return "20FebPm.txt"
        //to add: date creation,
    }
}