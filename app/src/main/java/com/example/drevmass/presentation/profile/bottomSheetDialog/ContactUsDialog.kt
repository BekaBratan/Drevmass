package com.example.drevmass.presentation.profile.bottomSheetDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentContactUsDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactUsDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentContactUsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactUsDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        binding.phoneBlock.setOnClickListener {
            val bottomSheetFragment = CallPhoneDialog()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }

        binding.supportCenterBlock.setOnClickListener {
            val bottomSheetFragment = SupportServiceDialog()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}