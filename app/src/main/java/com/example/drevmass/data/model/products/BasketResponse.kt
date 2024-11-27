package com.example.drevmass.data.model.products

data class BasketResponse(
    val basket: List<Basket>,
    val basket_price: Int,
    val bonus: Int,
    val count_products: Int,
    val discount: Int,
    val products: List<Product>,
    val total_price: Int,
    val used_bonus: Int
)