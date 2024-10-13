package com.example.drevmass.presentation.authorization

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentForgotPasswordBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ForgotPasswordBottomsheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentForgotPasswordBottomsheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBottomsheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                val layoutParams = it.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.FILL_PARENT // Allow it to expand based on content
                it.layoutParams = layoutParams

                // Set the BottomSheet behavior to expanded
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT)
    }

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForColorStateLists")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {

            etEmail.addTextChangedListener {
                if (etEmail.text.toString().isNotEmpty()) {
                    btnRecoverPass.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    btnRecoverPass.isEnabled = true
                } else {
                    btnRecoverPass.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    btnRecoverPass.isEnabled = false
                }
            }

            etEmail.addTextChangedListener {
                if (etEmail.text.toString().isNotEmpty()) {
                    setDrawableEnd(etEmail)
                }
            }

            etEmail.setOnFocusChangeListener { _, event ->
                if (event) {
                    normalEditText(etEmail)
                    if (etEmail.text.toString().isNotEmpty()){
                        setDrawableEnd(etEmail)
                    }
                } else {
                    removeDrawableEnd(etEmail)
                    if (!isValidEmail(etEmail.text.toString()) && etEmail.text.toString().isNotEmpty()) {
                        wrongEditText(etEmail)
                    }
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
                val bottomsheet = PasswordRecoveredBottomsheet()
                bottomsheet.show(childFragmentManager, bottomsheet.tag)
            }
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