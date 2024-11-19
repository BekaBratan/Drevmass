package com.example.drevmass.presentation.profile.promocode

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentPromocodeBinding
import com.example.drevmass.presentation.profile.bottomSheetDialog.InfoBonusProgramDialog
import com.example.drevmass.presentation.profile.promocode.promocodeBottomSheetDialog.PromocodeAppliedListener
import com.example.drevmass.presentation.profile.promocode.promocodeBottomSheetDialog.PromocodeBottomSheetDialog
import com.example.drevmass.presentation.utils.showSuccessCustomToast

class PromocodeFragment : Fragment() {

    private lateinit var binding: FragmentPromocodeBinding

    private lateinit var clipboardManager: ClipboardManager

    private lateinit var clipData: ClipData

    private val viewModel: PromocodeViewModel by viewModels()

    private val bottomSheetFragment = PromocodeBottomSheetDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPromocodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarPromocodeIncluded.icBack.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        val userToken = SharedProvider(requireContext()).getToken()
        if (userToken.isNullOrBlank() || userToken.isEmpty()) {
            findNavController().navigate(R.id.loginFragment)
        } else {
            viewModel.getUserPromocode(userToken)
        }
        bottomSheetFragment.setOnPromoCodeAppliedListener(object : PromocodeAppliedListener {
            override fun onPromocodeApplied(bonusIsUsed: Boolean) {
                Log.d("AAA", "bonusIsUsed + $bonusIsUsed")
                val customToast =
                    Toast.makeText(requireContext(), "Your message", Toast.LENGTH_SHORT)
                customToast.showSuccessCustomToast(
                    "Промокод успешно применен",
                    requireContext(),
                    this@PromocodeFragment
                )
            }
        })

        binding.toolbarPromocodeIncluded.icInfo.setOnClickListener {
            Log.d("bonus", "info button was clicked")
            val bottomSheetFragment = InfoBonusProgramDialog()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }

        viewModel.responsePromocode.observe(viewLifecycleOwner) {
            binding.promocode.visibility = View.VISIBLE
            binding.promocode.text = it.promocode
            binding.tvBonusUSedAllAttempt.text = "/${it.allAttempt}"
            binding.tvCountSharePromocode.text = it.used.toString()
            if (it.used == it.allAttempt) {
                binding.sectionPromocodeUnavailable.visibility = View.VISIBLE
                binding.promocodeItem.visibility = View.GONE
                binding.sectionInfoBlock.visibility = View.GONE
            } else {
                binding.sectionPromocodeUnavailable.visibility = View.GONE
                binding.promocodeItem.visibility = View.VISIBLE
                binding.sectionInfoBlock.visibility = View.VISIBLE
            }
        }

        binding.promocodeHaveBtn.setOnClickListener {
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }

        binding.shareBlock.setOnClickListener {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "Хей, я скачал приложение и пользуюсь, и ты скачай там бонусы и тд, вот мой промокод ${binding.promocode.text} \n" +
                        "Вот ссылка на скачивание: drevamss.google.play.kg")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.copyBlock.setOnClickListener{
            clipData = ClipData.newPlainText("text", "Ваш промокод: " + binding.promocode.text)
            clipboardManager.setPrimaryClip(clipData)

            val customToast = Toast.makeText(requireContext(), "Your message", Toast.LENGTH_SHORT)
            customToast.showSuccessCustomToast("Промокод успешно скопирован", requireContext(), this@PromocodeFragment)
        }
    }

}