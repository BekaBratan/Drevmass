package com.example.drevmass.presentation.course.courseInfo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentSelectDayBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectDayBottomSheet(
    private val onScheduleSavedListener: (List<String>) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSelectDayBottomSheetBinding
    private val listDays = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectDayBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            btnSelectMonday.setOnClickListener {
                toggleDaySelection(btnSelectMonday, "Пн")
            }
            btnSelectTuesday.setOnClickListener {
                toggleDaySelection(btnSelectTuesday, "Вт")
            }
            btnSelectWednesday.setOnClickListener {
                toggleDaySelection(btnSelectWednesday, "Ср")
            }
            btnSelectThursday.setOnClickListener {
                toggleDaySelection(btnSelectThursday, "Чт")
            }
            btnSelectFriday.setOnClickListener {
                toggleDaySelection(btnSelectFriday, "Пт")
            }
            btnSelectSaturday.setOnClickListener {
                toggleDaySelection(btnSelectSaturday, "Сб")
            }
            btnSelectSunday.setOnClickListener {
                toggleDaySelection(btnSelectSunday, "Вс")
            }
            btnSaveGraffik.setOnClickListener {
                onScheduleSavedListener.invoke(listDays)
                Log.d("AAA", "btnsheet: $listDays")
                dismiss()
            }
        }
    }

    private fun toggleDaySelection(selectBtnBinding: AppCompatButton, day: String) {
        if (listDays.contains(day)) {
            listDays.remove(day)
            unSelectDayBackground(selectBtnBinding)
        } else {
            listDays.add(day)
            selectDayBackground(selectBtnBinding)
        }
        Log.d("AAA", "Current listDays: $listDays")
    }

    private fun selectDayBackground(selectBtnBinding: AppCompatButton) {
        selectBtnBinding.setBackgroundResource(R.drawable.background_btn_day_selected)
    }

    private fun unSelectDayBackground(selectBtnBinding: AppCompatButton) {
        selectBtnBinding.setBackgroundResource(R.drawable.background_btn_day_unselected)
    }

}