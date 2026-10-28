package com.example.movilaw.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movilaw.native.NotificationHelper
import com.example.movilaw.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    notificationHelper: NotificationHelper,
    onNavigateToContacto: () -> Unit,
    onNavigateToMiEspacio: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val laws by mainViewModel.lawsList.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Catálogo Jurídico - Movilaw") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Botones de acceso rápido a otras secciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onNavigateToMiEspacio) { Text("Mi Espacio") }
                Button(onClick = onNavigateToContacto) { Text("Contacto") }
                Button(onClick = onNavigateToProfile) { Text("Perfil") }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para simular la función del RECURSO NATIVO
            OutlinedButton(
                onClick = {
                    notificationHelper.showLawUpdateNotification(
                        "Código Civil - Art. 1",
                        "Se ha registrado una nueva modificación normativa sintética."
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simular Alerta Nativa (Notificación)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Legislación Disponible:", style = MaterialTheme.typography.titleMedium)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(laws) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(item.referenceTitle, style = MaterialTheme.typography.titleSmall)
                            Text(item.legalCategory, style = MaterialTheme.typography.labelSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.content, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

