package com.example.drevmass.presentation.profile.myData

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.model.userInfo.UserInfoRequest
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentMyDataBinding
import com.example.drevmass.presentation.utils.provideNavigationHost
import com.google.android.material.textfield.TextInputEditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyDataFragment : Fragment() {

    private lateinit var binding: FragmentMyDataBinding

    private val calendar = Calendar.getInstance()

    private val viewModel: MyDataViewModel by viewModels()

    private var userId = 0
    private var previousUserName = ""
    private var previousUserNumber = ""
    private var previousUserEmail = ""
    private var previousUserDateOfBirth = ""
    private var previousUserHeight = 0
    private var previousUserWeight = 0
    private var afterUserName = ""
    private var afterUserNumber = ""
    private var afterUserEmail = ""
    private var afterUserDateOfBirth = ""
    private var afterUserHeight = 0
    private var afterUserWeight = 0
    private var previousUserGender = 0
    private var previousUserActivity = 0
    private var afterUserGender = 0
    private var afterUserActivity = 0


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

        activityViewMode()

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

        val userToken = SharedProvider(requireContext()).getToken()
        if (userToken.isNullOrBlank() || userToken.isEmpty()) {
            findNavController().navigate(R.id.loginFragment)
        } else {
            viewModel.getUserInfo(userToken)
        }

        viewModel.responseUserInfo.observe(viewLifecycleOwner) { userInfoData ->
            userId = userInfoData.id
            previousUserName = userInfoData.name
            previousUserEmail = userInfoData.email
            previousUserNumber = userInfoData.phoneNumber
            previousUserDateOfBirth = userInfoData.birth
            previousUserHeight = userInfoData.height
            previousUserWeight = userInfoData.weight
            previousUserGender = userInfoData.gender
            previousUserActivity = userInfoData.activity

            binding.textInputEditTextName.setText(userInfoData.name)
            binding.textInputEditTextPhoneNumber.setText(userInfoData.phoneNumber)
            binding.textInputEditTextEmail.setText(userInfoData.email)
            val convertedDate = if (userInfoData.birth.isNotEmpty()) {
                convertDateFormat(userInfoData.birth)
            } else {
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
                dateFormat.format(currentDate)
            }
            binding.textInputEditTextDateOfBirth.setText(convertedDate)

            binding.textInputEditTextDateOfBirth.setText(convertedDate)

            if (userInfoData.gender == 2) {
                binding.rbFemale.isChecked = true
                radioGroupGenderChangesListener(binding.rbFemale.id)
            } else if (userInfoData.gender == 1) {
                binding.rbMale.isChecked = true
                radioGroupGenderChangesListener(binding.rbMale.id)
            } else if (userInfoData.gender == 0) {
                binding.rbNotIdentified.isChecked = true
                radioGroupGenderChangesListener(binding.rbNotIdentified.id)
            }
            else{
                binding.rbNotIdentified.isChecked = true
                radioGroupGenderChangesListener(binding.rbNotIdentified.id)
            }

            binding.textInputEditTextHeight.setText(userInfoData.height.toString())
            binding.textInputEditTextWeight.setText(userInfoData.weight.toString())

            if (userInfoData.activity == 0) {
                binding.rbSmallActivity.isChecked = true
                radioGroupActivityChangesListener(binding.rbSmallActivity.id)
            } else if (userInfoData.activity == 1) {
                binding.rbMediumActivity.isChecked = true
                radioGroupActivityChangesListener(binding.rbMediumActivity.id)
            } else if(userInfoData.activity == 2) {}
            else{
                binding.rbHighActivity.isChecked = true
                radioGroupActivityChangesListener(binding.rbHighActivity.id)
            }
            saveChangesMyData()
        }
        observerErrorResponse()
        observerUpdateUserInfo()

        binding.btnSaveUserDataUpdates.setOnClickListener {
            Log.d("AAA", "Btn was clicked")
            val requestBody = autoObjectRequest()
            updateUserInfoData(userToken, requestBody)
        }
    }

    private fun autoObjectRequest(): UserInfoRequest {
        var userName = ""
        var userNumber = ""
        var userEmail = ""
        var userDateOfBirth = ""
        var userHeight = 0
        var userWeight = 0
        var userGender = 0
        var userActivity = 0

        binding.apply {
            userName = textInputEditTextName.text.toString()
            userNumber = textInputEditTextPhoneNumber.text.toString()
            userEmail = textInputEditTextEmail.text.toString()
            userDateOfBirth = reverseDateFormat(textInputEditTextDateOfBirth.text.toString())

            // Проверка корректности ввода для роста
            textInputEditTextHeight.text.toString().toIntOrNull()?.let {
                userHeight = it
            }

            // Проверка корректности ввода для веса
            textInputEditTextWeight.text.toString().toIntOrNull()?.let {
                userWeight = it
            }

            afterUserName = userName
            afterUserNumber = userNumber
            afterUserEmail = userEmail
            afterUserDateOfBirth = userDateOfBirth
            afterUserHeight = userHeight
            afterUserWeight = userWeight

            when (binding.radioGroupGender.checkedRadioButtonId) {
                R.id.rb_not_identified -> {
                    userGender = 0
                }

                R.id.rb_male -> {
                    userGender = 1
                }

                R.id.rb_female -> {
                    userGender = 2
                }
            }

            afterUserGender = userGender

            when (binding.radioGroupActivity.checkedRadioButtonId) {
                R.id.rb_small_activity -> {
                    userActivity = 0
                }

                R.id.rb_medium_activity -> {
                    userActivity = 1
                }

                R.id.rb_high_activity -> {
                    userActivity = 2
                }
            }

            afterUserActivity = userActivity
        }
        return UserInfoRequest(
            userId,
            userEmail,
            userName,
            userGender,
            userHeight,
            userWeight,
            userDateOfBirth,
            userActivity,
            userNumber
        )
    }

    private fun updateUserInfoData(token: String, requestBody: UserInfoRequest) {
        viewModel.updateUserInfo(token, requestBody)
    }

    private fun observerUpdateUserInfo() {
        viewModel.responsePostUserInfo.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun observerErrorResponse() {
        viewModel.errorResponse.observe(viewLifecycleOwner) {
            Log.d("AAA", " Error at VM : $it")
        }
    }

    private fun convertDateFormat(inputDate: String): String {
        if (inputDate.isEmpty()) {
            return "" // return an empty string or any default value
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

        try {
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date!!)
        } catch (e: ParseException) {
            // If parsing fails, return the original input date
            return inputDate
        }
    }

    private fun reverseDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
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