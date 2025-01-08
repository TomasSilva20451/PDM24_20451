package com.example.shop.data.remote.api

import com.example.shop.data.remote.model.ProductDto
import com.example.shop.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getProducts(): List<Product> {
        val snapshot = db.collection("products").get().await()
        return snapshot.documents.map { document ->
            val productDto = document.toObject(ProductDto::class.java)!!
            Product(
                id = document.id,  // O ID do documento é mapeado para o campo 'id' da classe Product
                title = productDto.title,
                price = productDto.price,
                description = productDto.description,
                imageUrl = productDto.imageUrl
            )
        }
    }
}