package com.example.drevmass.presentation.profile.changePassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentChangePasswordBinding
import com.example.drevmass.presentation.authorization.ForgotPasswordBottomsheet
import com.example.drevmass.presentation.utils.provideNavigationHost
import com.example.drevmass.presentation.utils.showCustomToast
import com.example.drevmass.presentation.utils.showSuccessCustomToast

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewMode()
        binding.toolbarChangePasswordIncluded.icBack.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.textInputEditTextCurrentPassword.onChange {
            updateButtonState()
        }
        binding.textInputEditTextCurrentPassword.onChange {
            updateButtonState()
        }

        binding.btnChangePassword.setOnClickListener {
            viewModel.resetPassword(
                SharedProvider(requireContext()).getToken(),
                binding.textInputEditTextCurrentPassword.text.toString(),
                binding.textInputEditTextNewPassword.text.toString()
            )
        }
        binding.btnChangePassword.isEnabled = false

        viewModel.errorResponse.observe(viewLifecycleOwner) {
            val customToast = Toast.makeText(requireContext(), "Your message", Toast.LENGTH_SHORT)
            customToast.showCustomToast("При смене пароля произошла ошибка, текущий пароль может быть неверным", requireContext(), this)
        }
        viewModel.successResponse.observe(viewLifecycleOwner) {
            val customToast = Toast.makeText(requireContext(), "Your message", Toast.LENGTH_SHORT)
            customToast.showSuccessCustomToast(it, requireContext(), this)
        }

        binding.forgotPassword.setOnClickListener {
            val bottomSheetFragment = ForgotPasswordBottomsheet()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }
    }

    fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun updateButtonState() {
        val isAllDataFilled =
            binding.textInputEditTextCurrentPassword?.text?.isNotEmpty() ?: false &&
                    binding.textInputEditTextNewPassword?.text?.isNotEmpty() ?: false &&
                    (binding.textInputEditTextCurrentPassword?.text?.length ?: 0) >= 6 &&
                    (binding.textInputEditTextNewPassword?.text?.length ?: 0) >= 6

        if (isAllDataFilled) {
            binding.btnChangePassword.setBackgroundResource(R.drawable.btn_filled_sign_in)
            binding.btnChangePassword.isEnabled = true
        } else {
            binding.btnChangePassword.setBackgroundResource(R.drawable.btn_default_sign_in)
            binding.btnChangePassword.isEnabled = false
        }
    }

    private fun activityViewMode() {
        provideNavigationHost()?.apply {
            fullScreenMode(false)
            hideBottomNavigationBar(false)
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            fullScreenMode(false)
            hideBottomNavigationBar(true)
            setNavigationVisibility(visible = false)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            fullScreenMode(false)
            hideBottomNavigationBar(true)
            setNavigationVisibility(visible = false)
        }
    }
}