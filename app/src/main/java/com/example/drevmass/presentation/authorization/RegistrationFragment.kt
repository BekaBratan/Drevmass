package com.example.drevmass.presentation.authorization

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private var keypadHeight = 0
    private var isKeypadOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open
                isKeypadOpen = true
                if (isAllFilled())
                    binding.btnContinue.translationY = -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
            } else {
                // Keyboard is closed
                isKeypadOpen = false
                binding.btnContinue.translationY = 0f // move the button up
            }
        }

        binding.run {
            etEmail.addTextChangedListener {
                if (isAllFilled()) {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    if (isKeypadOpen)
                        binding.btnContinue.translationY = -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    binding.btnContinue.translationY = 0f // move the button up
                }
            }

            etPassword.addTextChangedListener {
                if (isAllFilled()) {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    if (isKeypadOpen)
                        binding.btnContinue.translationY = -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    binding.btnContinue.translationY = 0f // move the button up
                }
            }

            etName.addTextChangedListener {
                if (isAllFilled()) {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    if (isKeypadOpen)
                        binding.btnContinue.translationY = -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    binding.btnContinue.translationY = 0f // move the button up
                }
            }

            etPhone.addTextChangedListener {
                if (isAllFilled()) {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    if (isKeypadOpen)
                        binding.btnContinue.translationY = -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    binding.btnContinue.translationY = 0f // move the button up
                }
            }

            tvLink.setOnClickListener { findNavController().navigate(R.id.action_registrationFragment_to_loginFragment) }
        }
    }

    private fun isAllFilled(): Boolean {
        return binding.etEmail.text.toString().isNotEmpty() &&
                binding.etName.text.toString().isNotEmpty() &&
                binding.etPhone.text.toString().isNotEmpty() &&
                binding.etPassword.text.toString().isNotEmpty()
    }

}