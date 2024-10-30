package com.example.drevmass.presentation.basket.make_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentMakeOrderBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MakeOrderBottomsheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMakeOrderBottomsheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeOrderBottomsheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnContinue.setOnClickListener {
                findNavController().navigate(R.id.catalogFragment)
            }
        }
    }
}