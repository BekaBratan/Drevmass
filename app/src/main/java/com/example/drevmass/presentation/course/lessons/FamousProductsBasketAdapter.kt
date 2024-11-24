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
import java.text.NumberFormat
import java.util.Locale

class FamousProductsBasketAdapter(
    private val context: Context,
    private val productList: List<getFamousProductsResponse>,
    lessonFragment: LessonFragment
) : RecyclerView.Adapter<FamousProductsBasketAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.imageView123)
        val productName: TextView = itemView.findViewById(R.id.title_recomend)
        val productPrice: TextView = itemView.findViewById(R.id.price_recomend)
        val productAddToBasketButton: ImageButton = itemView.findViewById(R.id.productAddToBasketButton)

        fun bind(product: getFamousProductsResponse) {
            productName.text = product.title
            val numberFormat = NumberFormat.getInstance(Locale.getDefault())
            numberFormat.minimumFractionDigits = 0
            val formattedPrice = numberFormat.format(product.price)
            productPrice.text = "${ formattedPrice} ₽"
            // Добавьте привязку других данных по аналогии

            Glide.with(itemView)
                .load("http://drevmasstestapi.mobydev.kz/" + product.image_src)
                .centerCrop()
                .into(productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_recomendation_adapter, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}