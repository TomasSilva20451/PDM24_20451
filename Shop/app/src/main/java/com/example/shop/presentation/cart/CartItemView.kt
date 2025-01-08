package com.example.shop.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartItemView(
    cartItem: CartItem,
    onAdd: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cartItem.productName)
                Text(text = "Quantidade: ${cartItem.quantity}")
                Text(text = "Preço unitário: ${cartItem.price}")
            }

            Text(
                text = "Total: ${cartItem.price * cartItem.quantity}",
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { onAdd(cartItem) }) {
                    Text("Adicionar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onRemove(cartItem) }) {
                    Text("Remover")
                }
            }
        }
    }
}