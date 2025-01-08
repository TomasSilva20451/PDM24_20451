package com.example.shop.presentation.admin

import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Adiciona produto ao Firestore caso o usuário seja admin.
     */
    fun addProductIfAdmin(
        title: String,
        price: Double,
        description: String,
        imageUrl: String,
        isAdmin: Boolean
    ) {
        if (!isAdmin) {
            println("Você não tem permissão para adicionar produtos.")
            return
        }

        // Se você tiver uma classe de domínio Product...
        val newProduct = Product(
            id = "",  // gerado depois pelo Firestore, caso queira
            title = title,
            price = price,
            description = description,
            imageUrl = imageUrl
        )

        // Agora adiciona no Firestore
        db.collection("products")
            .add(newProduct)
            .addOnSuccessListener { docRef ->
                println("Produto adicionado com ID: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                println("Erro ao adicionar produto: $e")
            }
    }
}