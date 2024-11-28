package com.example.drevmass.data.model.courseModel.getAllBasket

import com.google.gson.annotations.SerializedName

data class BasketProductList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("product_img")
    val productImg: String,
    @SerializedName("product_title")
    val productTitle: String
)