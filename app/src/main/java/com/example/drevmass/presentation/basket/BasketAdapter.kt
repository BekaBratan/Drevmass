package com.example.drevmass.presentation.basket

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drevmass.data.model.products.Basket
import com.example.drevmass.data.util.Constants.Companion.IMAGE_URL
import com.example.drevmass.databinding.ItemBasketBinding
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback
import com.example.drevmass.presentation.utils.RcViewItemClickIdCountCallback

class BasketAdapter: RecyclerView.Adapter<BasketAdapter.MyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Basket>() {
        override fun areItemsTheSame(
            oldItem: Basket,
            newItem: Basket
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Basket,
            newItem: Basket
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Basket>){
        differ.submitList(list)
    }

    private var listenerClickAtItem: RcViewItemClickIdCallback? = null
    fun setOnProductClickListener(listener: RcViewItemClickIdCallback) {
        this.listenerClickAtItem = listener
    }

    private var listenerClickAtMinus: RcViewItemClickIdCountCallback? = null
    fun setOnMinusClickListener(listener: RcViewItemClickIdCountCallback) {
        this.listenerClickAtMinus = listener
    }

    private var listenerClickAtPlus: RcViewItemClickIdCountCallback? = null
    fun setOnPlusClickListener(listener: RcViewItemClickIdCountCallback) {
        this.listenerClickAtPlus = listener
    }

    inner class MyViewHolder(private var binding: ItemBasketBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun onBind(item: Basket){
            binding.run {
                Glide.with(itemView.context)
                    .load(IMAGE_URL + item.product_img)
                    .into(ivProduct)

                tvProductName.text = item.product_title
                tvProductCount.text = "${item.count}"
                tvProductCost.text = "${item.price} ₽"

                ivProduct.setOnClickListener() {
                    listenerClickAtItem?.onClick(item.product_id)
                }
                tvProductName.setOnClickListener() {
                    listenerClickAtItem?.onClick(item.product_id)
                }

                btnPlus.setOnClickListener() {
                    listenerClickAtPlus?.onClick(item.product_id, tvProductCount.text.toString().toInt())
                    tvProductCount.text = "${tvProductCount.text.toString().toInt() + 1}"
                }
                btnMinus.setOnClickListener() {
                    listenerClickAtMinus?.onClick(item.product_id, tvProductCount.text.toString().toInt())
                    tvProductCount.text = "${tvProductCount.text.toString().toInt() - 1}"

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