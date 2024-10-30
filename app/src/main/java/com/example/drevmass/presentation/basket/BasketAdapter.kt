package com.example.drevmass.presentation.basket

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.databinding.ItemBasketBinding
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback

class BasketAdapter: RecyclerView.Adapter<BasketAdapter.MyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<String>){
        differ.submitList(list)
    }

    private var listenerClickAtItem: RcViewItemClickIdCallback? = null
    fun setOnProductClickListener(listener: RcViewItemClickIdCallback) {
        this.listenerClickAtItem = listener
    }

    inner class MyViewHolder(private var binding: ItemBasketBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun onBind(item: String){
            binding.tvProductName.text = item
            binding.btnPlus.setOnClickListener(){
                binding.tvProductCount.text = (binding.tvProductCount.text.toString().toInt() + 1).toString()
//                listenerClickAtItem?.onClick(
//                    binding.tvProductCost.text.toString().toInt() * binding.tvProductCount.text.toString().toInt()
//                )
            }
            binding.btnMinus.setOnClickListener(){
                if (binding.tvProductCount.text.toString().toInt() <= 1){
//                    itemView.visibility = View.GONE
//                    differ.currentList.remove(item)
                } else {
                    binding.tvProductCount.text = (binding.tvProductCount.text.toString().toInt() - 1).toString()
//                    listenerClickAtItem?.onClick(
//                        binding.tvProductCost.text.toString().toInt() * binding.tvProductCount.text.toString().toInt()
//                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }
}