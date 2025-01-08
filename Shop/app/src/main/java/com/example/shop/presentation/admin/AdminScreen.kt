package com.example.shop.presentation.admin

//import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shop.presentation.user.AuthViewModel
import com.example.shop.ui.theme.ShopTheme
//import com.example.shop.domain.model.Product

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    authViewModel: AuthViewModel
) {
    var productName = remember { mutableStateOf("") }
    var productPrice = remember { mutableStateOf("") }
    var productDescription = remember { mutableStateOf("") }
    var productImageUrl = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        // Campo para nome do produto
        TextField(
            value = productName.value,
            onValueChange = { productName.value = it },
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo para preço do produto
        TextField(
            value = productPrice.value,
            onValueChange = { productPrice.value = it },
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo para descrição do produto
        TextField(
            value = productDescription.value,
            onValueChange = { productDescription.value = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo para URL da imagem
        TextField(
            value = productImageUrl.value,
            onValueChange = { productImageUrl.value = it },
            label = { Text("URL da Imagem") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para adicionar o produto
        Button(onClick = {
            viewModel.addProductIfAdmin(
                title = productName.value,
                price = productPrice.value.toDoubleOrNull() ?: 0.0,
                description = productDescription.value,
                imageUrl = productImageUrl.value,
                isAdmin = authViewModel.isAdmin.value
            )
        }) {
            Text("Adicionar Produto")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAdminScreen() {
    ShopTheme {
        AdminScreen(viewModel = AdminViewModel(), authViewModel = AuthViewModel()) // Exemplo de como visualizar a tela no preview
    }
}