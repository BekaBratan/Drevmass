package com.example.drevmass.presentation.profile.bottomSheetDialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentCallPhoneDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CallPhoneDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCallPhoneDialogBinding

    private lateinit var clipboardManager: ClipboardManager

    private lateinit var clipData: ClipData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallPhoneDialogBinding.inflate(layoutInflater, container, false)
        copyNumber()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun copyNumber() {
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.contactPhoneNumber.setOnClickListener {
            clipData = ClipData.newPlainText("text", binding.contactPhoneNumber.text)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

}