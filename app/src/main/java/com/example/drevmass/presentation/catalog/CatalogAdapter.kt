package com.example.drevmass.presentation.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drevmass.R
import com.example.drevmass.data.model.products.Product
import com.example.drevmass.data.util.Constants.Companion.IMAGE_URL
import com.example.drevmass.databinding.ItemProductHorizontalBinding
import com.example.drevmass.databinding.ItemProductTileBinding
import com.example.drevmass.databinding.ItemProductVerticalBinding
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback

class CatalogAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var layoutNum = 1  // Variable to toggle between layouts

    companion object {
        private const val VIEW_TYPE_LAYOUT_1 = 1
        private const val VIEW_TYPE_LAYOUT_2 = 2
        private const val VIEW_TYPE_LAYOUT_3 = 3
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Product>){
        differ.submitList(list)
    }

    fun nextLayout() {
        layoutNum++
        if (layoutNum > 3) layoutNum = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(layoutNum) {
            1 -> VIEW_TYPE_LAYOUT_1
            2 -> VIEW_TYPE_LAYOUT_2
            3 -> VIEW_TYPE_LAYOUT_3
            else -> VIEW_TYPE_LAYOUT_1
        }
    }

    private var listenerClickAtItem: RcViewItemClickIdCallback? = null
    fun setOnItemClickListener(listener: RcViewItemClickIdCallback) {
        this.listenerClickAtItem = listener
    }
    private var listenerClickAtItemCart: RcViewItemClickIdCallback? = null
    fun setOnItemCartClickListener(listener: RcViewItemClickIdCallback) {
        this.listenerClickAtItemCart = listener
    }

    inner class TileViewHolder(private var binding: ItemProductTileBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: Product) {
            binding.run {
                Glide.with(itemView.context)
                    .load(IMAGE_URL + product.image_src)
                    .into(ivProduct)
                tvProductName.text = product.title
                tvProductCost.text = "${product.price}"
                if (product.basket_count > 0) {
                    ibAddToCart.background =
                        getDrawable(itemView.context, R.drawable.ic_busket_add_28)
                    ibAddToCart.isEnabled = false
                }
                ibAddToCart.setOnClickListener {
                    if (product.basket_count == 0) {
                        ibAddToCart.background =
                            getDrawable(itemView.context, R.drawable.ic_busket_add_28)
                        listenerClickAtItemCart?.onClick(product.id)
                        ibAddToCart.isEnabled = false
                    }
                }
            }
            itemView.setOnClickListener {
                listenerClickAtItem?.onClick(product.id)
            }
        }
    }

    inner class VerticalViewHolder(private var binding: ItemProductVerticalBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: Product) {
            binding.run {
                Glide.with(itemView.context)
                    .load(IMAGE_URL + product.image_src)
                    .into(ivProduct)
                tvProductName.text = product.title
                tvProductCost.text = "${product.price}"
            }
            itemView.setOnClickListener {
                listenerClickAtItem?.onClick(product.id)
            }
        }
    }

    inner class HorizontalViewHolder(private var binding: ItemProductHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: Product) {
            binding.run {
                Glide.with(itemView.context)
                    .load(IMAGE_URL + product.image_src)
                    .into(ivProduct)
                tvProductName.text = product.title
                tvProductCost.text = "${product.price}"
            }
            itemView.setOnClickListener {
                listenerClickAtItem?.onClick(product.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LAYOUT_1 -> TileViewHolder(
                ItemProductTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            VIEW_TYPE_LAYOUT_2 -> VerticalViewHolder(
                ItemProductVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> HorizontalViewHolder(
                ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (layoutNum) {
            1 -> onBindViewHolder(holder as TileViewHolder, position)
            2 -> onBindViewHolder(holder as VerticalViewHolder, position)
            3 -> onBindViewHolder(holder as HorizontalViewHolder, position)
        }
    }

    private fun onBindViewHolder(holder: CatalogAdapter.TileViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }

    private fun onBindViewHolder(holder: CatalogAdapter.VerticalViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }

    private fun onBindViewHolder(holder: CatalogAdapter.HorizontalViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getLayoutNum(): Int {
        return layoutNum
    }
}