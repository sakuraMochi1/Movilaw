package com.example.movilaw.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilaw.repository.LawRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: LawRepository) : ViewModel() {


    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _profileType = MutableStateFlow("Estudiante")
    val profileType: StateFlow<String> = _profileType.asStateFlow()


    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()


    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess: StateFlow<Boolean> = _isLoginSuccess.asStateFlow()


    private val _sessionCheckComplete = MutableStateFlow(false)
    val sessionCheckComplete: StateFlow<Boolean> = _sessionCheckComplete.asStateFlow()

    private val _hasActiveSession = MutableStateFlow(false)
    val hasActiveSession: StateFlow<Boolean> = _hasActiveSession.asStateFlow()

    init {
        viewModelScope.launch {
            val savedUser = repository.activeUser.first()
            _hasActiveSession.value = savedUser != null
            _sessionCheckComplete.value = true
        }
    }

    fun onEmailChanged(input: String) {
        _email.value = input
        if (input.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            _emailError.value = "Formato de correo electrónico inválido"
        } else {
            _emailError.value = null
        }
    }

    fun onPasswordChanged(input: String) {
        _password.value = input
        if (input.isNotEmpty() && input.length < 6) {
            _passwordError.value = "La contraseña debe tener al menos 6 caracteres"
        } else {
            _passwordError.value = null
        }
    }

    fun onProfileTypeChanged(type: String) {
        _profileType.value = type
    }

    fun login() {
        val currentEmail = _email.value
        val currentPassword = _password.value


        var isValid = true

        if (currentEmail.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            _emailError.value = "Ingresa un correo válido"
            isValid = false
        }

        if (currentPassword.isBlank() || currentPassword.length < 6) {
            _passwordError.value = "Ingresa una contraseña válida de al menos 6 caracteres"
            isValid = false
        }

        if (isValid) {
            viewModelScope.launch {

                repository.saveSession(
                    email = currentEmail,
                    name = currentEmail.substringBefore("@").replaceFirstChar { it.uppercase() },
                    profileType = _profileType.value
                )
                _hasActiveSession.value = true
                _isLoginSuccess.value = true
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit = {}) {
        viewModelScope.launch {

            repository.clearSession()
            _hasActiveSession.value = false
            _isLoginSuccess.value = false
            _password.value = ""
            onLoggedOut()
        }
    }
}