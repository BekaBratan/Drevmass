package com.example.drevmass.data.model.products

data class ProductDetailResponse(
    val Product: Product,
    val Recommend: List<Product>
)