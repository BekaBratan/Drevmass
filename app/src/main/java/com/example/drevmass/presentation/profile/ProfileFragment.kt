package com.example.drevmass.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentProfileBinding
import com.example.drevmass.presentation.utils.provideNavigationHost

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNavigationHost()?.hideBottomNavigationBar(false)
        provideNavigationHost()?.fullScreenMode(true)
        binding.blockPoints.setOnClickListener {
            findNavController().navigate(R.id.bonusFragment)
        }
        binding.promocodeBlock.setOnClickListener {
            findNavController().navigate(R.id.promocodeFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(true)
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(true)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(true)
        }
    }
}