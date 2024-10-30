package com.example.drevmass.presentation.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.databinding.ItemBasketBinding
import com.example.drevmass.databinding.ItemProductSimilarBinding
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback

class SimilarAdapter: RecyclerView.Adapter<SimilarAdapter.MyViewHolder>() {

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
    fun setOnMovieClickListener(listener: RcViewItemClickIdCallback) {
        this.listenerClickAtItem = listener
    }

    inner class MyViewHolder(private var binding: ItemProductSimilarBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(item: String){
            binding.tvProductName.text = item

            itemView.setOnClickListener {
                listenerClickAtItem?.onClick(item.length)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemProductSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }
}