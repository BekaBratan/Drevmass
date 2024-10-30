package com.example.drevmass.presentation.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    private var currentList: List<String> = emptyList()

    fun submitList(list: List<String>) {
        currentList = list
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

    inner class TileViewHolder(private var binding: ItemProductTileBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(itemName: String) {
            binding.run {
                tvProductName.text = itemName
            }
            itemView.setOnClickListener {
                listenerClickAtItem?.onClick(adapterPosition)
            }
        }
    }

    inner class VerticalViewHolder(private var binding: ItemProductVerticalBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(itemName: String) {
            binding.run {
                tvProductName.text = itemName
            }
            itemView.setOnClickListener {
                listenerClickAtItem?.onClick(adapterPosition)
            }
        }
    }

    inner class HorizontalViewHolder(private var binding: ItemProductHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(itemName: String) {
            binding.run {
                tvProductName.text = itemName
            }
            itemView.setOnClickListener {
                listenerClickAtItem?.onClick(adapterPosition)
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
        holder.onBind(currentList[position])
    }

    private fun onBindViewHolder(holder: CatalogAdapter.VerticalViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    private fun onBindViewHolder(holder: CatalogAdapter.HorizontalViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun getLayoutNum(): Int {
        return layoutNum
    }
}