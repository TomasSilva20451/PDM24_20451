package com.example.shop.presentation.cart

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shop.presentation.user.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun SharedCartsScreen(
    authViewModel: AuthViewModel
) {
    val currentUserEmail = authViewModel.userEmail.value.orEmpty()
    val sharedCartItems = remember { mutableStateOf<List<CartItem>>(emptyList()) }

    // Classe para deserialização
    data class Cart(
        val items: List<CartItem> = emptyList(), // Valor padrão
        val sharedWith: List<String> = emptyList() // Valor padrão
    ) {
        // Construtor sem argumentos para compatibilidade com o Firebase
        constructor() : this(emptyList(), emptyList())
    }

    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail.isNotBlank()) {
            try {
                val db = FirebaseFirestore.getInstance()
                // Pega todos os docs que contêm currentUserEmail no array "sharedWith"
                val querySnapshot = db.collection("carts")
                    .whereArrayContains("sharedWith", currentUserEmail)
                    .get()
                    .await()

                // Log para verificar a query
                Log.d("SharedCartsScreen", "Iniciando busca de carrinhos compartilhados para $currentUserEmail...")

                val allItems = mutableListOf<CartItem>()

                // Para cada documento, vamos adicionar os itens à lista
                for (doc in querySnapshot.documents) {
                    val cart = doc.toObject(Cart::class.java)
                    val ownerEmail = doc.id  // ID do documento: por exemplo, "tomasilva@gmail.com"
                    if (cart != null) {
                        allItems += cart.items
                        Log.d("SharedCartsScreen", "Carrinho de $ownerEmail carregado com os itens: ${cart.items}")
                    }
                }

                // Atualizando o estado com os itens compartilhados
                sharedCartItems.value = allItems

            } catch (e: Exception) {
                Log.e("SharedCartsScreen", "Erro ao carregar carrinhos compartilhados: $e")
            }
        }
    }

    // Layout da tela
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Carrinhos Compartilhados Com Você", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Exibe os carrinhos compartilhados
        if (sharedCartItems.value.isEmpty()) {
            Text("Nenhum carrinho compartilhado encontrado.")
        } else {
            sharedCartItems.value.forEach { item ->
                Text("Produto: ${item.productName}")
                Text("Quantidade: ${item.quantity}")
                Text("Preço: ${item.price}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}