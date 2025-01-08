package com.example.shop.presentation.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.State
import com.example.shop.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Estado para controlar se o usuário está logado
    private val _isUserLoggedIn = mutableStateOf(false)
    val isUserLoggedIn: State<Boolean> = _isUserLoggedIn

    // Estado para armazenar o e-mail do usuário logado
    private val _userEmail = mutableStateOf<String?>(null)
    val userEmail: State<String?> = _userEmail

    // Estado para verificar se o usuário é admin
    private val _isAdmin = mutableStateOf(false)
    val isAdmin: State<Boolean> = _isAdmin

    // Estado para mensagens de erro
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Função para fazer login
    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isUserLoggedIn.value = true
                    _userEmail.value = auth.currentUser?.email
                    _errorMessage.value = null
                    checkIfAdmin() // Verifica se o usuário é admin logo após o login
                } else {
                    _errorMessage.value = "Login falhou. Verifique suas credenciais."
                }
            }
    }

    // Função para verificar se o usuário é admin
    private fun checkIfAdmin() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val isAdmin = document.getBoolean("isAdmin") ?: false
                        _isAdmin.value = isAdmin
                    }
                }
                .addOnFailureListener {
                    // Tratar falha ao buscar no Firestore
                }
        }
    }


    // Função para registrar um novo usuário com email e senha
    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _errorMessage.value = null // Limpar erro em caso de sucesso
                } else {
                    _errorMessage.value = task.exception?.message
                }
            }
    }

    // Função para verificar se o usuário está logado
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Função para fazer logout
    fun logout() {
        auth.signOut()
        _isUserLoggedIn.value = false
        _userEmail.value = null
    }
}