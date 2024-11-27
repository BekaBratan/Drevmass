package com.example.drevmass.data.model.products

data class AddToCartRequest(
    val count: Int,
    val product_id: Int,
    val user_id: Int
)