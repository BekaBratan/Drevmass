package com.example.drevmass.presentation.course.lessons

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drevmass.R
import com.example.drevmass.data.model.courseModel.getFamousProductsBasket.getFamousProductsResponse
import com.example.drevmass.data.model.products.Product
import com.example.drevmass.data.util.Constants.Companion.IMAGE_URL
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.ItemProductSimilarBinding
import com.example.drevmass.databinding.LessonProductAdapterBinding
import com.example.drevmass.presentation.basket.SimilarAdapter
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback
import java.text.NumberFormat
import java.util.Locale
import kotlin.getValue

class FamousProductsBasketAdapter: RecyclerView.Adapter<FamousProductsBasketAdapter.MyViewHolder>() {

    private var listOfProducts: List<Product> = emptyList()

    fun submitList(list: List<Product>){
        listOfProducts = list
    }

    private var listenerClickAtItem: RcViewItemClickIdCallback? = null
    fun setOnItemClickListener(listener: RcViewItemClickIdCallback) {
        this.listenerClickAtItem = listener
    }
    private var listenerClickAtItemCart: RcViewItemClickIdCallback? = null
    fun setOnItemCartClickListener(listener: RcViewItemClickIdCallback) {
        this.listenerClickAtItemCart = listener
    }

    inner class MyViewHolder(private var binding: ItemProductSimilarBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun onBind(product: Product){
            binding.run {
                Glide.with(itemView.context)
                    .load(IMAGE_URL + product.image_src)
                    .into(ivProduct)
                tvProductName.text = product.title
                tvProductCost.text = "${product.price} ₽"
                Log.d("AAAA", "onBind: $product")
                if (product.basket_count > 0) {
                    ibAddToCart.background =
                        getDrawable(itemView.context, R.drawable.ic_basket_add_48)
                    ibAddToCart.isEnabled = false
                }
                ibAddToCart.setOnClickListener {
                    if (product.basket_count == 0) {
                        ibAddToCart.background =
                            getDrawable(itemView.context, R.drawable.ic_basket_add_48)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemProductSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(listOfProducts[position])
    }
}