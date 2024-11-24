package com.example.drevmass

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.ActivityMainBinding
import com.example.drevmass.presentation.authorization.ForgotPasswordBottomsheet
import com.example.drevmass.presentation.utils.NavigationHostProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationHostProvider {

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigationBarMainActivity.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.courseFragment -> {
                    navController.navigate(R.id.courseFragment)
                }

                R.id.catalogFragment -> {
                    navController.navigate(R.id.catalogFragment)
                }

                R.id.basketFragment -> {
                    navController.navigate(R.id.basketFragment)
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                }
                else -> null
            } != null
        }
    }

    override fun hideBottomNavigationBar(hide: Boolean) {
        if (hide) {
            binding.bottomNavigationBarMainActivity.visibility = View.GONE
        } else {
            binding.bottomNavigationBarMainActivity.visibility = View.VISIBLE
        }
    }

    override fun fullScreenMode(visible: Boolean) {
        if (visible) {
            window?.apply {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    decorView.windowInsetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            window?.apply {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = ContextCompat.getColor(
                    this@MainActivity,
                    R.color.white
                )
            }
        }
    }

    override fun statusBarColor(isWoody: Boolean) {
        if (isWoody) {
            window?.apply {
                statusBarColor = ContextCompat.getColor(
                    this@MainActivity,
                    R.color.woody
                )
            }
        } else {
            window?.apply {
                statusBarColor = ContextCompat.getColor(
                    this@MainActivity,
                    R.color.white
                )
            }
        }
    }

    override fun showErrorNotificationBar(message: String) {
        binding.notificationBar.ivNotification.setImageResource(R.drawable.ic_attention)
        binding.notificationBar.tvNotification.text = message
        binding.notificationBar.root.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coral_1000)

        lifecycleScope.launch {
            binding.notificationBar.root.visibility = View.VISIBLE
            delay(1500)
            binding.notificationBar.root.visibility = View.GONE
        }
    }

    override fun showSuccessNotificationBar(message: String) {
        binding.notificationBar.ivNotification.setImageResource(R.drawable.ic_success_24)
        binding.notificationBar.tvNotification.text = message
        binding.notificationBar.root.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green_1000)

        lifecycleScope.launch {
            binding.notificationBar.root.visibility = View.VISIBLE
            delay(1500)
            binding.notificationBar.root.visibility = View.GONE
        }
    }

    override fun setNavigationVisibility(visible: Boolean) {
        binding.bottomNavigationBarMainActivity.isVisible = visible
    }

    override fun orientationLandscape(landscape: Boolean) {
        if (landscape) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}