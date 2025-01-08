package com.example.shop.domain.repository

import com.example.shop.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>

    // Adiciona o método para escutar os produtos em tempo real
    fun listenToProducts(onProductsChanged: (List<Product>) -> Unit)
}