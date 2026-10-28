package com.example.movilaw.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacto Académico") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Envía una consulta jurídica o reporte técnico:")
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Escribe tu mensaje...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { if (messageText.isNotBlank()) isSubmitted = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Consulta")
            }

            if (isSubmitted) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("¡Consulta enviada exitosamente!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
