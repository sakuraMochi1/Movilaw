

package com.example.movilaw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilaw.model.Abogado
import com.example.movilaw.repository.AbogadosApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AbogadosViewModel(
    private val repository: AbogadosApiRepository = AbogadosApiRepository()
) : ViewModel() {

    private val _abogados = MutableStateFlow<List<Abogado>>(emptyList())
    val abogados: StateFlow<List<Abogado>> = _abogados.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        cargarAbogados()
    }

    fun cargarAbogados() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _abogados.value = repository.getAbogados()
            } catch (exception: Exception) {
                _error.value = exception.message
                    ?: "No fue posible cargar los abogados."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun crearAbogado(
        nombreAbogado: String,
        rut: String,
        numeroCaso: String,
        nombreCliente: String
    ) {
        if (
            nombreAbogado.isBlank() ||
            rut.isBlank() ||
            numeroCaso.isBlank() ||
            nombreCliente.isBlank()
        ) {
            _mensaje.value = "Completa todos los campos."
            return
        }

        val rutNumero = rut.toLongOrNull()
        val casoNumero = numeroCaso.toIntOrNull()

        if (rutNumero == null || casoNumero == null) {
            _mensaje.value = "El RUT y el número de caso deben ser numéricos."
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            _mensaje.value = null
            _error.value = null

            try {
                repository.crearAbogado(
                    nombreAbogado = nombreAbogado.trim(),
                    rut = rutNumero,
                    numeroCaso = casoNumero,
                    nombreCliente = nombreCliente.trim()
                )

                _abogados.value = repository.getAbogados()
                _mensaje.value = "Abogado agregado correctamente."

            } catch (exception: Exception) {
                _mensaje.value = exception.message
                    ?: "No fue posible guardar el abogado."
            } finally {
                _isSaving.value = false
            }
        }
    }
}

