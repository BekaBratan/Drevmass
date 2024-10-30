package com.example.drevmass.presentation.profile.bottomSheetDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentAboutCompanyDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AboutCompanyDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAboutCompanyDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutCompanyDialogBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation

        binding.tvCloseAboutCompanyDialog.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}