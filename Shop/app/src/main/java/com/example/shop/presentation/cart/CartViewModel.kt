package com.example.shop.presentation.cart

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.Product
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
) {
    companion object {
        fun fromProduct(product: Product): CartItem {
            return CartItem(
                productId = product.id,
                productName = product.title,
                quantity = 1,
                price = product.price
            )
        }
    }
}

class CartViewModel : ViewModel() {

    // Representa o estado local do carrinho atual
    private val _cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val cartItems: State<List<CartItem>> = _cartItems

    // Classe que espelha o formato salvo no Firestore
    data class Cart(
        val items: List<CartItem> = emptyList(),
        val sharedWith: List<String> = emptyList()
    )

    private val db = FirebaseFirestore.getInstance()

    // Carrega (ou cria) o carrinho de um usuário (owner)
    fun loadUserCart(userEmail: String) {
        if (userEmail.isBlank()) return

        val cartRef = db.collection("carts").document(userEmail)
        cartRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val cart = document.toObject(Cart::class.java)
                    val items = cart?.items ?: emptyList()
                    _cartItems.value = items
                    Log.d("CartViewModel", "Carrinho de $userEmail carregado: ${_cartItems.value}")
                } else {
                    // Se não existe, cria documento vazio
                    saveCartToFirestore(userEmail)
                    Log.d("CartViewModel", "Nenhum carrinho encontrado para $userEmail. Criando vazio.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("CartViewModel", "Erro ao carregar carrinho: $e")
            }
    }

    // Salva o carrinho atual no Firestore
    fun saveCartToFirestore(userEmail: String) {
        if (userEmail.isBlank()) return

        val cartData = hashMapOf(
            "items" to _cartItems.value.map {
                hashMapOf(
                    "productId" to it.productId,
                    "productName" to it.productName,
                    "quantity" to it.quantity,
                    "price" to it.price
                )
            },
            "sharedWith" to emptyList<String>()
        )

        db.collection("carts").document(userEmail)
            .set(cartData)
            .addOnSuccessListener {
                Log.d("CartViewModel", "Carrinho salvo para $userEmail com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.e("CartViewModel", "Erro ao salvar carrinho: $e")
            }
    }

    // Adiciona item no carrinho do usuário e salva
    fun addItemToCart(item: CartItem, userEmail: String) {
        val existingItem = _cartItems.value.find { it.productId == item.productId }
        if (existingItem != null) {
            _cartItems.value = _cartItems.value.map { oldItem ->
                if (oldItem.productId == item.productId) {
                    oldItem.copy(quantity = oldItem.quantity + 1)
                } else oldItem
            }
        } else {
            _cartItems.value += item
        }
        saveCartToFirestore(userEmail)
        Log.d("CartViewModel", "Carrinho atualizado: ${_cartItems.value}")
    }

    // Remove item do carrinho
    fun removeItemFromCart(item: CartItem, userEmail: String) {
        _cartItems.value = _cartItems.value.filter { it.productId != item.productId }
        saveCartToFirestore(userEmail)
        Log.d("CartViewModel", "Item removido: ${item.productName}")
    }

    // Compartilha o carrinho do userEmail com sharedWithEmail
    fun shareCartWithUser(userEmail: String, sharedWithEmail: String) {
        if (userEmail.isBlank()) return
        Log.d("CartViewModel", "Compartilhando carrinho de $userEmail com $sharedWithEmail") // Log para depuração
        val cartRef = db.collection("carts").document(userEmail)
        cartRef.update("sharedWith", FieldValue.arrayUnion(sharedWithEmail))
            .addOnSuccessListener {
                Log.d("CartViewModel", "Carrinho de $userEmail compartilhado com $sharedWithEmail")
            }
            .addOnFailureListener { e ->
                Log.e("CartViewModel", "Erro ao compartilhar carrinho: $e")
            }
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.price * it.quantity }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun logout() {
        clearCart()
    }
}