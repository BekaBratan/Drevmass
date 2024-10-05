package com.example.drevmass.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentBasketBinding
import com.example.drevmass.presentation.utils.provideNavigationHost

class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNavigationHost()?.hideBottomNavigationBar(false)
        provideNavigationHost()?.fullScreenMode(false)
    }

    override fun onStart() {
        super.onStart()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(false)
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(false)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(false)
        }
    }

}