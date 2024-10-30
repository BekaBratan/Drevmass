package com.example.drevmass.presentation.profile.myData

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentMyDataBinding
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyDataFragment : Fragment() {

    private lateinit var binding: FragmentMyDataBinding

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyDataBinding.inflate(layoutInflater, container, false)
        binding.btnSaveUserDataUpdates.isEnabled = false
        return binding.root
    }

    private fun saveChangesMyData() {
        editTextChangesListener(binding.textInputEditTextName)
        editTextChangesListener(binding.textInputEditTextPhoneNumber)
        editTextChangesListener(binding.textInputEditTextEmail)
        editTextChangesListener(binding.textInputEditTextDateOfBirth)
        editTextChangesListener(binding.textInputEditTextHeight)
        editTextChangesListener(binding.textInputEditTextWeight)
    }

    private fun radioGroupGenderChangesListener(defaultCheckedId: Int) {
        binding.radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != defaultCheckedId) {
                updateButtonBackground()
            }
        }
    }

    private fun radioGroupActivityChangesListener(defaultCheckedId: Int) {
        binding.radioGroupActivity.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != defaultCheckedId) {
                updateButtonBackground()
            }
        }
    }

    private fun editTextChangesListener(changedData: TextInputEditText) {
        changedData.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButtonBackground()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim() // Trimmed to remove leading and trailing whitespaces

                if (text.isNullOrEmpty()) {
                    binding.textInputLayoutName.errorIconDrawable?.setTint(Color.RED) // Set error icon color to red
                }
            }
        })
    }

    private fun updateButtonBackground() {
        val isAllDataFilled = binding.textInputEditTextName?.text?.isNotEmpty() ?: false &&
                binding.textInputEditTextPhoneNumber?.text?.isNotEmpty() ?: false  &&
                binding.textInputEditTextEmail?.text?.isNotEmpty() ?: false  &&
                binding.textInputEditTextDateOfBirth?.text?.isNotEmpty() ?: false &&
                binding.textInputEditTextHeight?.text?.isNotEmpty() ?: false &&
                binding.textInputEditTextWeight?.text?.isNotEmpty() ?: false

        if (isAllDataFilled){
            binding.btnSaveUserDataUpdates.setBackgroundResource(R.drawable.btn_filled_sign_in)
            binding.btnSaveUserDataUpdates.isEnabled = true
        } else {
            // If any field is empty, keep the button disabled
            binding.btnSaveUserDataUpdates.setBackgroundResource(R.drawable.btn_default_sign_in)
            binding.btnSaveUserDataUpdates.isEnabled = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMyDataIncluded.icBack.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.textInputEditTextDateOfBirth.setOnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.textInputEditTextDateOfBirth.right - binding.textInputEditTextDateOfBirth.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    showDatePicker()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        binding.tvDeleteAccount.setOnClickListener {
            val message: String? =
                "Ваши личные данные и накопленные бонусы будут удалены без возможности восстановления."
            showCustomDialogBox(message)
        }
    }

    private fun showCustomDialogBox(message: String?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_account_custom_dialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        val tvMessage: TextView = dialog.findViewById(R.id.tv_message_alert)
        val btnSafeAccount: TextView = dialog.findViewById(R.id.btn_safe_account)
        val btnDeleteAccount: TextView = dialog.findViewById(R.id.btn_delete_account)

        tvMessage.text = message

        btnSafeAccount.setOnClickListener {
            dialog.dismiss()
        }

        btnDeleteAccount.setOnClickListener {
            Toast.makeText(requireContext(), "click on Delete", Toast.LENGTH_LONG).show()
        }

        dialog.show()
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
                val formattedDate: String = dateFormat.format(selectedDate.time)
                binding.textInputEditTextDateOfBirth.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}