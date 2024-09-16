package com.example.drevmass.presentation.profile.bonus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.databinding.AdapterRcHistoryItemBinding

class HistoryItemAdapter(): RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterRcHistoryItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRcHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {return 0}


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

}