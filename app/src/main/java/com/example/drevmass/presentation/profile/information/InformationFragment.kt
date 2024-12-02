package com.example.drevmass.presentation.profile.information

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentInformationBinding
import com.example.drevmass.presentation.profile.bottomSheetDialog.AboutAppDialog
import com.example.drevmass.presentation.profile.bottomSheetDialog.AboutCompanyDialog
import com.example.drevmass.presentation.utils.provideNavigationHost

class InformationFragment : Fragment() {

    private lateinit var binding: FragmentInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewMode()
        binding.toolbarInfoIncluded.icBack.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.aboutCompanyLayout.setOnClickListener {
            val bottomSheetFragment = AboutCompanyDialog()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }

        binding.aboutAppLayout.setOnClickListener {
            val bottomSheetFragment = AboutAppDialog()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }

        binding.idVk.setOnClickListener {
        }
        binding.icYoutube.setOnClickListener {
        }
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