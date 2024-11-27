package com.example.drevmass.data.model.products

data class Basket(
    val count: Int,
    val price: Int,
    val product_id: Int,
    val product_img: String,
    val product_title: String
)