package com.example.drevmass.presentation.profile.bonus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.R
import com.example.drevmass.data.model.bonus.TransactionX
import com.example.drevmass.databinding.AdapterRcHistoryItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryItemAdapter(): RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>() {

    private var listOfBonus = arrayListOf<TransactionX>()

    fun submitListOfBonus(bonusList: List<TransactionX>){
        listOfBonus.addAll(bonusList)
    }

    class ViewHolder(private val binding: AdapterRcHistoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: TransactionX) {
            if (item.transactionType.equals("+")) {
                //Log.d("dd", item.toString())
                binding.bonusDesc.text = item.description
                val convertedDate = convertDateFormat(item.transactionDate)
                binding.dateOfTrans.text = convertedDate
                binding.bonusPoint.text = "+" + item.promoPrice.toString()
            } else {
                binding.bonusDesc.text = item.description
                val convertedDate = convertDateFormat(item.transactionDate)
                binding.dateOfTrans.text = convertedDate
                binding.bonusPoint.text = "-" + item.promoPrice.toString()
                val color = ContextCompat.getColor(binding.root.context, R.color.bonusRed)
                binding.bonusPoint.setTextColor(color)
            }
        }

        private fun convertDateFormat(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRcHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listOfBonus.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfBonus[position]
        holder.bind(item)
    }

}