package com.example.drevmass.presentation.authorization

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.transition.Visibility
import com.example.drevmass.databinding.FragmentForgotPasswordBottomsheetBinding
import com.example.drevmass.presentation.utils.provideNavigationHost
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.example.drevmass.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("DEPRECATION")
class ForgotPasswordBottomsheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentForgotPasswordBottomsheetBinding
    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBottomsheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val layoutParams = it.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.FILL_PARENT // Allow it to expand based on content
                it.layoutParams = layoutParams

                // Set the BottomSheet behavior to expanded
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForColorStateLists")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {

            etEmail.addTextChangedListener {
                if (etEmail.text.toString().isNotEmpty()) {
                    tvEmailHint.visibility = View.VISIBLE
                    setDrawableEnd(etEmail)
                    btnRecoverPass.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    btnRecoverPass.isEnabled = true
                } else {
                    removeDrawableEnd(etEmail)
                    tvEmailHint.visibility = View.GONE
                    btnRecoverPass.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    btnRecoverPass.isEnabled = false
                }
            }

            etEmail.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP && etEmail.compoundDrawables[2] != null) {
                    val drawableEnd = etEmail.compoundDrawables[2]
                    if (event.rawX >= (etEmail.right - drawableEnd.bounds.width())) {
                        clearText(etEmail)
                        return@setOnTouchListener true
                    }
                }
                false
            }

            btnRecoverPass.setOnClickListener {
                viewModel.recoverPassword(etEmail.text.toString())
            }
        }

        viewModel.authorizationResponse.observe(viewLifecycleOwner) {
            val bottomsheet = PasswordRecoveredBottomsheet("На почту ${binding.etEmail.text} мы отправили инструкцию для сброса пароля.")
            bottomsheet.show(childFragmentManager, bottomsheet.tag)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) {
            binding.tvEmailHint.visibility = View.VISIBLE
            binding.tvEmailHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.coral_1000))
            binding.etEmail.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.coral_1000)
        }
    }

    private fun clearText(editText: EditText) {
        editText.text.clear()
        removeDrawableEnd(editText)
    }

    private fun setDrawableEnd(editText: EditText) {
        val drawableEnd = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableEnd, null)
    }

    private fun removeDrawableEnd(editText: EditText) {
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists", "UseCompatTextViewDrawableApis")
    private fun wrongEditText(editText: EditText) {
        editText.compoundDrawableTintList = resources.getColorStateList(R.color.coral_1000)
        editText.backgroundTintList = resources.getColorStateList(R.color.coral_1000)
    }

    @SuppressLint("UseCompatTextViewDrawableApis", "UseCompatLoadingForColorStateLists")
    private fun normalEditText(editText: EditText) {
        editText.compoundDrawableTintList = null
        editText.backgroundTintList = resources.getColorStateList(R.color.gray_500)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}