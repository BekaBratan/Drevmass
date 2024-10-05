package com.example.drevmass.presentation.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.drevmass.R


fun Toast.showSuccessCustomToast(message: String, context: Context, fragment:Fragment) {

    val layoutInflater = LayoutInflater.from(context)
    val layout = layoutInflater.inflate(
        R.layout.above_sheet_success,
        fragment.requireView().findViewById(R.id.successOperation)
    )

    val textView = layout.findViewById<TextView>(R.id.successTxt)
    textView.text = message

    val popupWindow = PopupWindow(
        layout,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    popupWindow.showAtLocation(fragment.requireView(), Gravity.TOP, 0, 0)

    Handler(Looper.getMainLooper()).postDelayed({
        popupWindow.dismiss()
    }, 2000)} // 2000 миллисекунд (2 секунды)