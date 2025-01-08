package com.example.shop.data.remote.repository

import com.example.shop.data.remote.api.FirebaseService
import com.example.shop.domain.model.Product
import com.example.shop.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepositoryImpl(private val firebaseService: FirebaseService) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        // Chama o FirebaseService para buscar os produtos
        return firebaseService.getProducts()
    }

    override fun listenToProducts(onProductsChanged: (List<Product>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        // Escuta os produtos em tempo real
        db.collection("products")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Lidar com erro
                    return@addSnapshotListener
                }
                val products = snapshot?.documents?.map { document ->
                    Product(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        price = document.getDouble("price") ?: 0.0,
                        description = document.getString("description") ?: "",
                        imageUrl = document.getString("imageUrl") ?: ""
                    )
                } ?: emptyList()

                // Chamando o callback para passar os produtos
                onProductsChanged(products)
            }
    }
}