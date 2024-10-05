package com.example.drevmass

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.drevmass.databinding.ActivityMainBinding
import com.example.drevmass.presentation.utils.NavigationHostProvider

class MainActivity : AppCompatActivity(), NavigationHostProvider {

    private var binding: ActivityMainBinding? = null

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding?.bottomNavigationBarMainActivity?.setOnNavigationItemSelectedListener {
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
            binding?.bottomNavigationBarMainActivity?.visibility = View.GONE
        } else {
            binding?.bottomNavigationBarMainActivity?.visibility = View.VISIBLE
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
}