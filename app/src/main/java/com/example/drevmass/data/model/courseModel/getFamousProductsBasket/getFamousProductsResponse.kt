package com.example.drevmass.data.model.courseModel.getFamousProductsBasket

import com.google.gson.annotations.SerializedName

data class getFamousProductsResponse(
    @SerializedName("basket_count")
    val basketCount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("height")
    val height: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("image_src")
    val image_src: String,
    @SerializedName("video_src")
    val video_src: String,
    @SerializedName("viewed")
    val viewed: Int,
    var isAddedToCart: Boolean = false
)