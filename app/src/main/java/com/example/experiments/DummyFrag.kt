package com.example.experiments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.experiments.databinding.TestFragmentBinding

/*
 * Created by Sudhanshu Kumar on 19/06/24.
 */

class DummyFrag : Fragment() {

    private var _binding: TestFragmentBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TestFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        Log.d("DummyFrag", "onResume: $this")
    }

    override fun onPause() {
        super.onPause()
        Log.d("DummyFrag", "onPause: $this")
    }

}