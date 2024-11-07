package com.example.drevmass.presentation.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentPasswordRecoveredBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PasswordRecoveredBottomsheet(val email: String) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPasswordRecoveredBottomsheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPasswordRecoveredBottomsheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvRecoverPassMsg.text = email

        binding.btnContinue.setOnClickListener {
            dismiss()
            (parentFragment as ForgotPasswordBottomsheet).dismiss()
        }
    }

}