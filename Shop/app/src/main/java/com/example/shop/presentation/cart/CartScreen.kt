package com.example.shop.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shop.presentation.user.AuthViewModel

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val userEmail = authViewModel.userEmail.value ?: ""
    // Carrega o carrinho do userEmail quando a tela abre
    LaunchedEffect(userEmail) {
        if (userEmail.isNotBlank()) {
            viewModel.loadUserCart(userEmail)
        }
    }

    val cartItems = viewModel.cartItems.value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Meu Carrinho", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(cartItems) { cartItem ->
                CartItemView(
                    cartItem = cartItem,
                    onAdd = { viewModel.addItemToCart(it, userEmail) },
                    onRemove = { viewModel.removeItemFromCart(it, userEmail) }
                )
            }
        }

        val totalPrice = viewModel.getTotalPrice()
        Text(
            text = "Total: $totalPrice",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
        )

        // Botão para compartilhar (vai para a tela de inserir email)
        Button(
            onClick = { navController.navigate("shareCart") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Compartilhar Carrinho")
        }

        // Botão para ver "Carrinhos compartilhados comigo"
        Button(
            onClick = { navController.navigate("sharedCarts") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Carrinhos Compartilhados Comigo")
        }

        // Botão para voltar p/ lista de produtos
        Button(
            onClick = { navController.navigate("productList") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Voltar para lista de produtos")
        }
    }
}