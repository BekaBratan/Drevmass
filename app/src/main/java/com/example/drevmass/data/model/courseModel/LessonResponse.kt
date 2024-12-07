package com.example.drevmass.data.model.courseModel


import com.example.drevmass.data.model.courseModel.getFamousProductsBasket.getFamousProductsResponse
import com.example.drevmass.data.model.products.Product
import com.example.drevmass.data.model.products.ProductsResponse
import com.google.gson.annotations.SerializedName
data class LessonResponse(
    @SerializedName("id")
    val id: Int, // 11
    @SerializedName("name")
    val name: String, // awe
    @SerializedName("title")
    val title: String, // qw
    @SerializedName("description")
    val description: String,
    @SerializedName("order_id")
    val orderId: Int?,// qw
    @SerializedName("image_src")
    val imageSrc: String, // ./lessons/images/awe/Screenshot 2024-01-15 at 10.35.36 PM.png
    @SerializedName("video_src")
    val videoSrc: String, // qw
    @SerializedName("duration")
    val duration: Int, // 11
    @SerializedName("is_favorite")
    val isFavorite: Boolean, // false
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("used_products")
    val usedProducts: List<Product>
)