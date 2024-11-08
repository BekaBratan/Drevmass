package com.example.drevmass.presentation.utils

interface NavigationHostProvider {
    fun hideBottomNavigationBar(visible: Boolean)
    fun fullScreenMode(visible: Boolean)
    fun statusBarColor(isWoody: Boolean)
    fun showErrorNotificationBar(message: String)
    fun showSuccessNotificationBar(message: String)
}