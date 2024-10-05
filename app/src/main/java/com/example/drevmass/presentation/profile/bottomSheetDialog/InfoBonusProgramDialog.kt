package com.example.drevmass.presentation.profile.bottomSheetDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.drevmass.R
import com.example.drevmass.databinding.InfoBonusProgramBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBonusProgramDialog: BottomSheetDialogFragment() {
    private lateinit var binding: InfoBonusProgramBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InfoBonusProgramBinding.inflate(inflater, container, false)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation

        binding.toolbarInfoBonusProgramIncluded.icBackToMyPointsPage.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}