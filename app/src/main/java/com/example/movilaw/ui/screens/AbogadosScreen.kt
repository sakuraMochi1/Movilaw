

package com.example.movilaw.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movilaw.viewmodel.AbogadosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbogadosScreen(
    viewModel: AbogadosViewModel,
    onBack: () -> Unit
) {
    val abogados by viewModel.abogados.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var nombreAbogado by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var numeroCaso by remember { mutableStateOf("") }
    var nombreCliente by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Abogados") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Agregar nuevo abogado",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = nombreAbogado,
                onValueChange = { nombreAbogado = it },
                label = { Text("Nombre del abogado") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT (sin puntos ni guion)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = numeroCaso,
                onValueChange = { numeroCaso = it },
                label = { Text("Número de caso") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = nombreCliente,
                onValueChange = { nombreCliente = it },
                label = { Text("Nombre del cliente") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    viewModel.crearAbogado(
                        nombreAbogado,
                        rut,
                        numeroCaso,
                        nombreCliente
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar abogado")
                }
            }

            mensaje?.let {
                Text(
                    text = it,
                    color = if (it == "Abogado agregado correctamente.")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Abogados registrados",
                    style = MaterialTheme.typography.titleMedium
                )

                TextButton(onClick = viewModel::cargarAbogados) {
                    Text("Actualizar")
                }
            }

            if (isLoading) {
                CircularProgressIndicator()
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(abogados, key = { it.id }) { abogado ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                abogado.nombre_abogado,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("RUT: ${abogado.rut}")
                            Text("Número de caso: ${abogado.numero_caso}")
                            Text("Cliente: ${abogado.nombre_cliente}")
                        }
                    }
                }
            }
        }
    }
}
