package com.example.shop.presentation.product_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shop.domain.model.Product
import com.example.shop.presentation.cart.CartItem
import com.example.shop.presentation.cart.CartViewModel

@Composable
fun ProductListScreen(viewModel: ProductListViewModel, cartViewModel: CartViewModel, userId: String) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(viewModel.products.value) { product ->
            val cartItem = CartItem(
                productId = product.id,
                productName = product.title,
                quantity = 1,
                price = product.price
            )
            ProductItemView(product = product, onAdd = {
                // Passando o userId para a função addItemToCart
                cartViewModel.addItemToCart(cartItem, userId)
            })
        }
    }
}

@Composable
fun ProductItemView(product: Product, onAdd: (CartItem) -> Unit) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = product.title)
            Text(text = "Preço: ${product.price}")
            Button(onClick = { onAdd(CartItem.fromProduct(product)) }) {
                Text("Adicionar ao Carrinho")
            }
        }
    }
}