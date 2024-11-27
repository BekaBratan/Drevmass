package com.example.drevmass.data.model.products

data class Product(
    val basket_count: Int,
    val description: String,
    val height: String,
    val id: Int,
    val image_src: String,
    val price: Int,
    val size: String,
    val title: String,
    val video_src: String,
    val viewed: Int?
)