package com.example.drevmass.presentation.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

object InternetUtil {

    private lateinit var application: Application
    fun init(application: Application) {
        InternetUtil.application = application
    }

    fun isInternetOn(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}