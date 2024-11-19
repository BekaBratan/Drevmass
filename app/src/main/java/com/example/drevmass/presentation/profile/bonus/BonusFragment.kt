package com.example.drevmass.presentation.profile.bonus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentBonusBinding
import com.example.drevmass.presentation.profile.bottomSheetDialog.InfoBonusProgramDialog
import java.text.SimpleDateFormat
import java.util.Locale

class BonusFragment : Fragment() {

    private lateinit var binding: FragmentBonusBinding

    private val viewModel: BonusViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBonusBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarBonusIncluded.icBack.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.toolbarBonusIncluded.icInfo.setOnClickListener {
            val bottomSheetFragment = InfoBonusProgramDialog()
            bottomSheetFragment.showNow(parentFragmentManager, bottomSheetFragment.tag)
        }

        val shared = SharedProvider(requireContext())
        val userToken = shared.getToken()
        if (userToken.isNullOrBlank() || userToken.isEmpty()) {
            findNavController().navigate(R.id.loginFragment)
        } else {
            viewModel.getBonus(userToken)
        }

        viewModel.responseBonus.observe(viewLifecycleOwner) { bonusData ->
            binding.textView.text = "1 балл = 1 ₽"

            Log.d("MyPointsFragment", "No burning data found + ${bonusData.burning.isNotEmpty()}")
            if (bonusData.burning.isNotEmpty()) {
                binding.deadlineBonusSection.visibility = View.VISIBLE
                for (burningItem in bonusData.burning) {
                    val bonus = burningItem.bonus
                    val burningDate = burningItem.burningDate

                    binding.price.text = bonus.toString()

                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

                    val date = inputFormat.parse(burningDate)
                    val formattedDate = outputFormat.format(date)

                    Log.d("MyPointsFragment", "Bonus: $bonus, Burning Date: $formattedDate")
                    binding.deadline.text = "сгорят ${formattedDate.toString()}"
                }
            } else {
                binding.deadlineBonusSection.visibility = View.GONE
                Log.d("MyPointsFragment", "No burning data found")
            }

            Log.d("bb", "data: $bonusData")
            if (bonusData.bonus != 0 || bonusData.transactions.isNotEmpty()) {
                binding.emptyState.visibility = View.GONE
                binding.bonusInfoRc.visibility = View.VISIBLE
                binding.bonus.text = bonusData.bonus.toString()
                Log.d("bbbbb", "data: ${bonusData.bonus}")
                val adapter = HistoryItemAdapter()
                binding.bonusInfoRc.adapter = adapter
                adapter.submitListOfBonus(bonusData.transactions)
            } else {
                // Если список транзакций пуст, скройте содержимое
                binding.bonus.text = bonusData.bonus.toString()
                binding.emptyState.visibility = View.VISIBLE
                binding.bonusInfoRc.visibility = View.GONE
            }
        }
    }

}