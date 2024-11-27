package com.example.drevmass.presentation.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentSortBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomsheet(val sortParameter: String) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSortBottomsheetBinding
    private var sortCallback: SortCallback? = null

    interface SortCallback {
        fun onSortSelected(sortOption: String)
    }

    fun setSortCallback(callback: SortCallback) {
        sortCallback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSortBottomsheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            if (sortParameter == getString(R.string.sort_by_popularity)) {
                ivSortByPopularity.background = getDrawable(requireContext(), R.drawable.ic_radiobox_checked_24)
            } else if (sortParameter == getString(R.string.sort_by_price)) {
                ivSortByPrice.background = getDrawable(requireContext(), R.drawable.ic_radiobox_checked_24)
            } else if (sortParameter == getString(R.string.sort_by_price_down)) {
                ivSortByPriceDown.background = getDrawable(requireContext(), R.drawable.ic_radiobox_checked_24)
            }

            llSortByPopularity.setOnClickListener {
                sortCallback?.onSortSelected(getString(R.string.sort_by_popularity))
                dismiss()
            }
            llSortByPrice.setOnClickListener {
                sortCallback?.onSortSelected(getString(R.string.sort_by_price))
                dismiss()
            }
            llSortByPriceDown.setOnClickListener {
                sortCallback?.onSortSelected(getString(R.string.sort_by_price_down))
                dismiss()
            }
        }
    }
}