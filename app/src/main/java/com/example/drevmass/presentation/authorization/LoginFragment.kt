package com.example.drevmass.presentation.authorization

import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentLoginBinding
import com.example.drevmass.presentation.utils.provideNavigationHost

@Suppress("DEPRECATION")
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var keypadHeight = 0
    private var isKeypadOpen = false
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForColorStateLists", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNavigationHost()?.hideBottomNavigationBar(true)
        provideNavigationHost()?.fullScreenMode(true)

        binding.btnContinue.setOnClickListener { findNavController().navigate(R.id.courseFragment) }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open
                isKeypadOpen = true
                if (isAllFilled())
                    binding.btnContinue.translationY =
                        -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
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
                        binding.btnContinue.translationY =
                            -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    binding.btnContinue.translationY = 0f // move the button up
                }
            }

            etPassword.addTextChangedListener {
                if (isAllFilled()) {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    if (isKeypadOpen)
                        binding.btnContinue.translationY =
                            -keypadHeight.toFloat() + 56 + 68 + 32 // move the button up
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    binding.btnContinue.translationY = 0f // move the button up
                }
            }

            tvLink.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registrationFragment) }

            binding.etPassword.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawableEnd = binding.etPassword.compoundDrawables[2]
                    if (event.rawX >= (binding.etPassword.right - drawableEnd.bounds.width())) {
                        // Toggle password visibility
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
                false
            }

            binding.run {
                etEmail.addTextChangedListener {
                    if (etEmail.text.toString().isNotEmpty()) {
                        setDrawableEnd(etEmail)
                    }
                }

                etEmail.setOnFocusChangeListener { _, event ->
                    if (event) {
                        normalEditText(etEmail)
                        if (etEmail.text.toString().isNotEmpty()) {
                            setDrawableEnd(etEmail)
                        }
                    } else {
                        removeDrawableEnd(etEmail)
                        if (!isValidEmail(etEmail.text.toString()) && etEmail.text.toString()
                                .isNotEmpty()
                        ) {
                            wrongEditText(etEmail)
                        }
                    }
                }

                etEmail.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        val drawableEnd = binding.etPassword.compoundDrawables[2]
                        if (event.rawX >= (binding.etPassword.right - drawableEnd.bounds.width())) {
                            clearText(etEmail)
                            return@setOnTouchListener true
                        }
                    }
                    false
                }
            }
        }
    }

    private fun isAllFilled(): Boolean {
        return binding.etEmail.text.toString().isNotEmpty() &&
                binding.etPassword.text.toString().isNotEmpty()
    }


    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide the password
            binding.etPassword.transformationMethod = PasswordTransformationMethod()
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lock,
                0,
                R.drawable.ic_show,
                0
            )
        } else {
            // Show the password
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod()
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lock,
                0,
                R.drawable.ic_hide,
                0
            )
        }
        // Move the cursor to the end of the text
        binding.etPassword.setSelection(binding.etPassword.text.length)
        isPasswordVisible = !isPasswordVisible
    }


    override fun onStart() {
        super.onStart()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(true)
            provideNavigationHost()?.fullScreenMode(true)
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(true)
            provideNavigationHost()?.fullScreenMode(true)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(true)
            provideNavigationHost()?.fullScreenMode(true)
        }
    }

    private fun clearText(editText: EditText) {
        editText.text.clear()
        val drawableStart = editText.compoundDrawables[0]
        editText.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, null, null)
    }

    private fun setDrawableEnd(editText: EditText) {
        val drawableStart = editText.compoundDrawables[0]
        val drawableEnd = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
        editText.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, drawableEnd, null)
    }

    private fun removeDrawableEnd(editText: EditText) {
        val drawableStart = editText.compoundDrawables[0]
        editText.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, null, null)
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
