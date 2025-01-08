package com.example.shop.presentation.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shop.ui.theme.ShopTheme
import androidx.compose.runtime.mutableStateOf

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    // Observe se o login foi bem-sucedido
    val isLoggedIn = viewModel.isUserLoggedIn.value

    // Exibe a tela de login se o usuário não estiver logado
    if (!isLoggedIn) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.loginUser(email.value, password.value) }) {
                Text("Login")
            }

            // Exibir mensagens de erro, se houver
            viewModel.errorMessage.value?.let {
                Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    ShopTheme {
        LoginScreen(viewModel = AuthViewModel())
    }
}