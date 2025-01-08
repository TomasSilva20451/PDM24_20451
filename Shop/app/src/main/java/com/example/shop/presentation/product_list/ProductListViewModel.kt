package com.example.shop.presentation.product_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.Product
import com.example.shop.domain.repository.ProductRepository

class ProductListViewModel(private val productRepository: ProductRepository) : ViewModel() {

    val products = mutableStateOf<List<Product>>(emptyList())

    init {
        // Escutando os produtos em tempo real
        productRepository.listenToProducts { updatedProducts ->
            products.value = updatedProducts
        }
    }
}