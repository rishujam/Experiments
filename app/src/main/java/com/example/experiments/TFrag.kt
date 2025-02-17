package com.example.experiments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.experiments.databinding.TFragBinding
import com.example.experiments.databinding.TestFragmentBinding
import com.example.experiments.imageeditor.PhotoEditActivity

/*
 * Created by Sudhanshu Kumar on 28/02/24.
 */

class TFrag : Fragment() {

    private var _binding: TFragBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AppStartup", "onCreate frag: ${System.currentTimeMillis()}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TFragBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Lifecycle", "onViewCreated TFrag")
        binding?.btnNext?.setOnClickListener {
            val intent = Intent(requireActivity(), PhotoEditActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume TFrag")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause TFrag")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "onStop TFrag")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "onStart TFrag")
    }

}