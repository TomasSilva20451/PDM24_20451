package com.example.shop.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shop.presentation.admin.AdminScreen
import com.example.shop.presentation.user.AuthViewModel
import com.example.shop.presentation.cart.CartViewModel
import com.example.shop.presentation.product_list.ProductListScreen
import com.example.shop.presentation.cart.CartScreen
import com.example.shop.presentation.user.LoginScreen
import com.example.shop.presentation.product_list.ProductListViewModel
import com.example.shop.presentation.admin.AdminViewModel
import com.example.shop.presentation.cart.ShareCartScreen
import com.example.shop.presentation.cart.SharedCartsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel,
    productViewModel: ProductListViewModel,
    adminViewModel: AdminViewModel // Passando o AdminViewModel para MainScreen
) {
    val navController = rememberNavController()

    // Verifique se o usuário está logado e é admin
    if (authViewModel.isUserLoggedIn.value) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Bem-vindo, ${authViewModel.userEmail.value}")
                    },
                    actions = {
                        // Botão de Logout
                        Button(onClick = {
                            authViewModel.logout()
                            Log.d("MainScreen", "Usuário deslogado")
                        }) {
                            Text("Logout")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Botão de “Ver Carrinho”
                        Button(onClick = {
                            Log.d("MainScreen", "Navegando para o Carrinho")
                            navController.navigate("cart")
                        }) {
                            Text("Ver Carrinho")
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Conteúdo principal dentro do Scaffold
            Box(modifier = Modifier.padding(innerPadding)) {
                // Configuração da navegação
                NavHost(navController = navController, startDestination = "productList") {
                    composable("productList") {
                        ProductListScreen(
                            viewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            userId = authViewModel.userEmail.value ?: ""  // Passando o userId autenticado
                        )
                    }
                    composable("cart") {
                        // Passa o navController como parâmetro
                        CartScreen(
                            viewModel = cartViewModel,
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }
                    composable("shareCart") {
                        // A tela ShareCartScreen será chamada aqui
                        ShareCartScreen(
                            authViewModel = authViewModel,
                            cartViewModel = cartViewModel,
                            navController = navController
                        )
                    }
                    // Adiciona a tela de carrinhos compartilhados
                    composable("sharedCarts") {
                        SharedCartsScreen(authViewModel = authViewModel)
                    }
                }
            }
        }

        // Mostrar a tela de administração se o usuário for admin
        if (authViewModel.isAdmin.value) {
            AdminScreen(
                viewModel = adminViewModel,
                authViewModel = authViewModel  // <-- Passar este parâmetro
            )
        }
    } else {
        // Caso contrário, exibe a tela de login
        LoginScreen(viewModel = authViewModel)
    }
}