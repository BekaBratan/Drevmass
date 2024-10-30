package com.example.drevmass.presentation.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentPromocodeBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PromocodeBottomsheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPromocodeBottomsheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromocodeBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

}