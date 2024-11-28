package com.example.drevmass.data.model.courseModel.getAllBasket

import com.example.drevmass.data.model.courseModel.getAllProducts.GetAllProductsResponse
import com.google.gson.annotations.SerializedName

data class GetAllBasketResponse(
    @SerializedName("basket")
    val basket: List<BasketProductList>?,
    @SerializedName("basket_price")
    val basketPrice: Int?,
    @SerializedName("bonus")
    val bonus: Int?,
    @SerializedName("count_products")
    val countProducts: Int?,
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("products")
    val products: List<GetAllProductsResponse>?,
    @SerializedName("total_price")
    val totalPrice: Int?,
    @SerializedName("used_bonus")
    val usedBonus: Int?
)