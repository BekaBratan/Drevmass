package com.example.drevmass.presentation.authorization

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.model.LoginRequest
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentLoginBinding
import com.example.drevmass.presentation.utils.provideNavigationHost

@Suppress("DEPRECATION")
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var keypadHeight = 0f
    private var translationHeight = 0f
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

    @SuppressLint("UseCompatLoadingForColorStateLists", "ClickableViewAccessibility",
        "SuspiciousIndentation"
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNavigationHost()?.hideBottomNavigationBar(true)
        provideNavigationHost()?.fullScreenMode(true)

        val sharedProvider = SharedProvider(requireContext())
        binding.run {

            btnContinue.setOnClickListener {
                val loginRequest = LoginRequest(
                    deviceToken = "deviceToken",
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString()
                )
                viewModel.login(loginRequest)
            }

            viewModel.authorizationResponse.observe(viewLifecycleOwner) {
                sharedProvider.saveToken(it.access_token.toString())
                findNavController().navigate(R.id.courseFragment)
            }

            viewModel.errorResponse.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            }

            root.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                root.getWindowVisibleDisplayFrame(rect)
                val screenHeight = root.rootView.height.toFloat()
                keypadHeight = screenHeight - rect.bottom
                translationHeight = -keypadHeight.toFloat() + btnContinue.height.toFloat() + btnContinue.marginBottom.toFloat()

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is open
                    isKeypadOpen = true
                    if (isAllFilled()) {
                        btnContinue.isEnabled = true
                        btnContinue.translationY = translationHeight
                    }
                } else {
                    // Keyboard is closed
                    isKeypadOpen = false
                    btnContinue.translationY = 0f
                }
            }

            etEmail.addTextChangedListener {
                if (isAllFilled()) {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    btnContinue.isEnabled = true
                    if (isKeypadOpen)
                        btnContinue.translationY = translationHeight
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    btnContinue.isEnabled = false
                    btnContinue.translationY = 0f
                }
            }

            etPassword.addTextChangedListener {
                if (isAllFilled()) {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    btnContinue.isEnabled = true
                    if (isKeypadOpen)
                        btnContinue.translationY = translationHeight
                } else {
                    btnContinue.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    btnContinue.isEnabled = false
                    btnContinue.translationY = 0f
                }
            }

            tvLink.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registrationFragment) }

            etPassword.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawableEnd = etPassword.compoundDrawables[2]
                    if (event.rawX >= (etPassword.right - drawableEnd.bounds.width())) {
                        // Toggle password visibility
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
                false
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

            tvForgotPass.setOnClickListener {
                val bottomsheet = ForgotPasswordBottomsheet()
                bottomsheet.show(childFragmentManager, bottomsheet.tag)
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
