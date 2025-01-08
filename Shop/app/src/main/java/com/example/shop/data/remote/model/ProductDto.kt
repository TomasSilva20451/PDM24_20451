package com.example.shop.data.remote.model

data class ProductDto(
    val id: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: String = ""
)