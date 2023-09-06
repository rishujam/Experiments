package com.example.experiments.userstorynew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.experiments.R
import com.example.experiments.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/*
 * Created by Sudhanshu Kumar on 10/08/23.
 */

class ReactionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val binding get() = _binding

    override fun getTheme(): Int {
        return R.style.CustomBottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}