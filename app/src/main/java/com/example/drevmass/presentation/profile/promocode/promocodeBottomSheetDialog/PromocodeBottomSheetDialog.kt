package com.example.drevmass.presentation.profile.promocode.promocodeBottomSheetDialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.drevmass.R
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.PromocodeBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.promocode.PromocodeError

class PromocodeBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: PromocodeBottomSheetDialogBinding

    private val viewModel: PromocodeViewModel by viewModels()

    private var mListener: PromocodeAppliedListener? = null

    fun setOnPromoCodeAppliedListener(listener: PromocodeAppliedListener) {
        mListener = listener
    }

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PromocodeBottomSheetDialogBinding.inflate(inflater, container, false)

        binding.promocodeEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // Галочка на клавиатуре была нажата
                if(binding.promocodeEditText.text.toString().isEmpty()){
                    val colorStateList = ColorStateList.valueOf(Color.RED)
                    binding.divider.backgroundTintList = colorStateList
                    binding.txtPromocode.visibility = View.VISIBLE
                    binding.promocodeEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_promocode_error, 0, 0, 0)
                }
                // Скрыть клавиатуру
                hideKeyboard()

                // Убрать фокус с EditText
                binding.promocodeEditText.clearFocus()

                return@setOnEditorActionListener true
            }
            false
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return binding.root
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.promocodeEditText.windowToken, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val shared = SharedProvider(requireContext())
        token = shared.getToken()

        view.post {
            val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = bottomSheet?.let { it1 -> BottomSheetBehavior.from(it1) }
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        clearPromocode()

        binding.promocodeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable?.length ?: 0 > 0) {
                    binding.clearPromocode.visibility = View.VISIBLE
                    binding.divider.setBackgroundResource(R.drawable.divider);
                    binding.promocodeEditText.onFocusChangeListener =
                        View.OnFocusChangeListener { _, hasFocus ->
                            binding.clearPromocode.visibility =
                                if (hasFocus && binding.promocodeEditText.text.isNotEmpty()) View.VISIBLE else View.INVISIBLE
                        }
                } else {
                    binding.clearPromocode.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.promocodeEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_promocode_24, 0, 0, 0)
                val color = ContextCompat.getColor(requireContext(), R.color.woody)
                val colorStateList = ColorStateList.valueOf(color)
                binding.divider.backgroundTintList = colorStateList
                binding.clearPromocode.visibility = View.GONE
                binding.txtPromocode.visibility = View.INVISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        binding.btnApply.setOnClickListener {
            if (binding.promocodeEditText.text.toString().isEmpty()) {
                val colorStateList = ColorStateList.valueOf(Color.RED)
                binding.divider.backgroundTintList = colorStateList
                binding.txtPromocode.text = "Введите промокод"
                binding.txtPromocode.visibility = View.VISIBLE
                binding.promocodeEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_promocode_error, 0, 0, 0)
            } else {
                val promocode = binding.promocodeEditText.text.toString()
                Log.d("TAG", "Promocode: $promocode") // Добавляем лог для отслеживания значения промокода
                sendPromocodeRequest(promocode)
            }
        }
    }

    private fun clearPromocode() {
        binding.clearPromocode.setOnClickListener {
            binding.promocodeEditText?.text = null
            binding.clearPromocode.visibility = View.GONE
        }
    }

    private fun sendPromocodeRequest(promocode: String) {
        val retrofit = ServiceBuilder.api
        lifecycleScope.launch {
            try {
                val response = retrofit.activatePromocode(token = "Bearer $token", promocode)

                if (response.isSuccessful) {
                    Log.d("TAG", "Промокод успешно применен")
                    applyPromoCode()
                    dismiss()
                   } else {
                    Log.d("TAG", "Произошла ошибка при применении промокода: ${response.code()}") // Добавляем лог для отслеживания ошибки
                    // Добавьте здесь код для обработки неуспешного запроса
                    val colorStateList = ColorStateList.valueOf(Color.RED)
                    binding.divider.backgroundTintList = colorStateList
                    val errorBody: PromocodeError = response.errorBody()?.string().let { GsonBuilder().create().fromJson(it, PromocodeError::class.java)}
                    binding.txtPromocode.text = errorBody.message
                    binding.txtPromocode.visibility = View.VISIBLE
                    binding.promocodeEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_promocode_error, 0, 0, 0)
                }
            } catch (e: Exception) {
                Log.e("TAG", "Ошибка при выполнении запроса: ${e.message}", e) // Добавляем лог для отслеживания исключения
            }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun applyPromoCode() {
        // Логика применения промокода

        // Вызываем обновление бонусов через слушатель
        mListener?.onPromocodeApplied(true)
    }
}