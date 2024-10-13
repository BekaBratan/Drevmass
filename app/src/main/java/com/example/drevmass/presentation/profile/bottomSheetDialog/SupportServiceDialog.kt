package com.example.drevmass.presentation.profile.bottomSheetDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentSupportServiceDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SupportServiceDialog : BottomSheetDialogFragment()  {

    private lateinit var binding: FragmentSupportServiceDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportServiceDialogBinding.inflate(layoutInflater, container, false)
        click()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation

        binding.toolbarSupportServiceIncluded.icBack.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    fun click() {
        binding.editeTextSupportService.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) { return@setOnEditorActionListener true }
            false
        }
    }

}