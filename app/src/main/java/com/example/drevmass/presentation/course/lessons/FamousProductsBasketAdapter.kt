package com.example.drevmass.presentation.course.lessons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drevmass.R
import com.example.drevmass.data.model.courseModel.getFamousProductsBasket.getFamousProductsResponse
import com.example.drevmass.databinding.LessonProductAdapterBinding
import java.text.NumberFormat
import java.util.Locale

class FamousProductsBasketAdapter(
    private val context: Context,
    private val productList: List<getFamousProductsResponse>,
    lessonFragment: LessonFragment
) : RecyclerView.Adapter<FamousProductsBasketAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private var binding: LessonProductAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: getFamousProductsResponse) {
            binding.tvProductTitle.text = product.title
            val numberFormat = NumberFormat.getInstance(Locale.getDefault())
            numberFormat.minimumFractionDigits = 0
            val formattedPrice = numberFormat.format(product.price)
            binding.tvProductPrice.text = "${ formattedPrice} ₽"
            // Добавьте привязку других данных по аналогии

            Glide.with(itemView)
                .load("http://drevmasstestapi.mobydev.kz/" + product.image_src)
                .centerCrop()
                .into(binding.imgProductPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LessonProductAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}