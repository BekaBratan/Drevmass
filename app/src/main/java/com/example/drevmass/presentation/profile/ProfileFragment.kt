package com.example.drevmass.presentation.profile

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentProfileBinding
import com.example.drevmass.presentation.profile.bottomSheetDialog.ContactUsDialog
import com.example.drevmass.presentation.utils.provideNavigationHost
import retrofit2.HttpException

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

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
        binding.shimmerProfileUserInfo.startShimmer()
        binding.shimmerBonusBall.startShimmer()
        binding.blockPoints.setOnClickListener {
            findNavController().navigate(R.id.bonusFragment)
        }
        binding.promocodeBlock.setOnClickListener {
            findNavController().navigate(R.id.promocodeFragment)
        }
        binding.myDataBlock.setOnClickListener {
            findNavController().navigate(R.id.myDataFragment)
        }
        binding.changePassBlock.setOnClickListener {
            findNavController().navigate(R.id.changePasswordFragment)
        }
        binding.logoutBlock.setOnClickListener {
            SharedProvider(requireContext()).setToken("without_token")
            SharedProvider(requireContext()).clearShared()
            findNavController().navigate(R.id.splashScreenFragment)
        }
        binding.contactUsBlock.setOnClickListener {
            val bottomSheetFragment = ContactUsDialog()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }
        binding.infoBlock.setOnClickListener {
            findNavController().navigate(R.id.informationFragment)
        }
        binding.notifBlock.setOnClickListener {
            findNavController().navigate(R.id.notificationFragment)
        }
        binding.feedbackBlock.setOnClickListener {
        }

        val shared = SharedProvider(requireContext())
        val userToken = shared.getToken()
        if (userToken.isNullOrBlank() || userToken.isEmpty()) {
            findNavController().navigate(R.id.loginFragment)
        } else {
            viewModel.getUserInfo(userToken)
            viewModel.getBonus(userToken)
        }

        viewModel.responseUserInfo.observe(viewLifecycleOwner) { userInfoData ->
            binding.tvUserName.text = userInfoData.name
            binding.tvPhoneNum.text = userInfoData.phoneNumber
            binding.shimmerProfileUserInfo.stopShimmer()
            binding.shimmerProfileUserInfo.visibility = View.GONE
        }

        viewModel.responseBonus.observe(viewLifecycleOwner) {
            Log.d("AAA", "Bonus observer $it")
            binding.bonusBall.text = it.bonus.toString()
            binding.shimmerBonusBall.stopShimmer()
            binding.shimmerBonusBall.visibility = View.GONE
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) {}
    }

    private fun showCustomDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logout_custom_dialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        val btnStayInAccount: TextView = dialog.findViewById(R.id.btn_stay_in_app)
        val btnLogout: TextView = dialog.findViewById(R.id.btn_logout)

        btnStayInAccount.setOnClickListener {
            dialog.dismiss()
        }

        btnLogout.setOnClickListener {
            dialog.dismiss()
            SharedProvider(requireContext()).clearShared()
            findNavController().navigate(R.id.onboardingFragment)
        }

        dialog.show()
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
