package com.example.shop.presentation.user

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun UserScreen(viewModel: UserViewModel) {
    val user = viewModel.userData

    // Exibe o nome do usuário (exemplo)
    Text(text = "Nome do utilizador: ${user.name}")
    // Outros campos, como email ou foto de perfil, podem ser adicionados aqui
}