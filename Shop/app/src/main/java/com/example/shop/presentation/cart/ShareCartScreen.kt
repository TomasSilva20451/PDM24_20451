package com.example.shop.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shop.presentation.user.AuthViewModel

@Composable
fun ShareCartScreen(
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel,
    navController: NavHostController
) {
    val emailToShare = remember { mutableStateOf("") }

    val ownerEmail = authViewModel.userEmail.value ?: ""

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Compartilhar Carrinho", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Input: Email de quem vai receber
        TextField(
            value = emailToShare.value,
            onValueChange = { emailToShare.value = it },
            label = { Text("Email do usuário para compartilhar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para compartilhar
        Button(
            onClick = {
                val targetEmail = emailToShare.value
                if (ownerEmail.isNotBlank() && targetEmail.isNotBlank()) {
                    cartViewModel.shareCartWithUser(ownerEmail, targetEmail)
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compartilhar Carrinho")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para voltar
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar")
        }
    }
}