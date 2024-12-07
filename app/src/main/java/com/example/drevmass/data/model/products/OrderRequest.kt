package com.example.drevmass.data.model.products

data class OrderRequest(
    val bonus: Int,
    val crmlink: String,
    val email: String,
    val phone_number: String,
    val products: List<OrderProducts>,
    val total_price: Int,
    val username: String
)