package com.example.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.ui.theme.ShopTheme
import com.example.shop.presentation.user.AuthViewModel
import com.example.shop.presentation.cart.CartViewModel
import com.example.shop.presentation.product_list.ProductListViewModel
import com.example.shop.data.remote.api.FirebaseService
import com.example.shop.data.remote.repository.ProductRepositoryImpl
import com.example.shop.presentation.MainScreen
import com.example.shop.presentation.admin.AdminViewModel
import com.example.shop.presentation.product_list.ProductListViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopTheme {
                val authViewModel: AuthViewModel = viewModel()
                val cartViewModel: CartViewModel = viewModel()

                // Criação do ProductRepository e ProductListViewModelFactory
                val firebaseService = FirebaseService()
                val productRepository = ProductRepositoryImpl(firebaseService)

                val productViewModel: ProductListViewModel = viewModel(
                    factory = ProductListViewModelFactory(productRepository)
                )

                // Instanciando o AdminViewModel
                val adminViewModel: AdminViewModel = viewModel()

                // Verifique se o usuário está logado
                LaunchedEffect(authViewModel.isUserLoggedIn.value) {
                    if (!authViewModel.isUserLoggedIn.value) {
                        // Aqui você pode garantir que, ao não estar logado, a tela de login será exibida automaticamente.
                    }
                }

                // Passando as dependências para a MainScreen
                MainScreen(
                    authViewModel = authViewModel,
                    cartViewModel = cartViewModel,
                    productViewModel = productViewModel,
                    adminViewModel = adminViewModel
                )
            }
        }
    }
}