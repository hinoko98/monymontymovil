package com.monteflor.monymontymovil.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monteflor.monymontymovil.data.AuthRepository
import com.monteflor.monymontymovil.data.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel(private val auth: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false)
        private set
    var loading by mutableStateOf(false)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set

    /** Error general (credenciales, red, servidor). Se muestra en un aviso sobre el formulario. */
    var formError by mutableStateOf<String?>(null)
        private set

    /** Sube en cada intento fallido: la pantalla lo observa para disparar la sacudida. */
    var errorTick by mutableStateOf(0)
        private set

    fun onEmail(value: String) {
        email = value
        emailError = null
        formError = null
    }

    fun onPassword(value: String) {
        password = value
        passwordError = null
        formError = null
    }

    fun togglePassword() {
        passwordVisible = !passwordVisible
    }

    fun submit(onSuccess: () -> Unit) {
        if (loading) return
        // Mismas reglas que la API (email válido, contraseña de 8+) para no gastar una petición en un error evidente.
        emailError = if (Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(email.trim())) null else "Escribe un correo válido, como nombre@correo.com"
        passwordError = if (password.length >= 8) null else "La contraseña tiene al menos 8 caracteres"
        if (emailError != null || passwordError != null) {
            errorTick++
            return
        }
        loading = true
        formError = null
        viewModelScope.launch {
            when (val result = auth.login(email, password)) {
                LoginResult.Success -> onSuccess()
                is LoginResult.Failure -> {
                    formError = result.message
                    errorTick++
                }
            }
            loading = false
        }
    }

    class Factory(private val auth: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = LoginViewModel(auth) as T
    }
}
