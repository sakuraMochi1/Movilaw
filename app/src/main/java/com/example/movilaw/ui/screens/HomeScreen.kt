package com.example.movilaw.ui.screens

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.movilaw.native.NotificationHelper
import com.example.movilaw.viewmodel.MainViewModel
import androidx.compose.ui.res.painterResource
import com.example.movilaw.R

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
    var capturedPhoto by remember { mutableStateOf<Bitmap?>(null) }
    var cameraError by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        capturedPhoto = bitmap
        cameraError = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Movilaw",
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = painterResource(id = R.drawable.movilaw_logo),
                            contentDescription = "Logo de Movilaw",
                            modifier = Modifier.size(180.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onNavigateToMiEspacio) { Text("Mi Espacio") }
                Button(onClick = onNavigateToContacto) { Text("Contacto") }
                Button(onClick = onNavigateToProfile) { Text("Perfil") }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    cameraError = null
                    try {
                        cameraLauncher.launch(null)
                    } catch (_: ActivityNotFoundException) {
                        cameraError = "No se encontró una aplicación de cámara en este dispositivo."
                    } catch (_: SecurityException) {
                        cameraError = "No se pudo abrir la cámara. Revisa la configuración del dispositivo."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Abrir cámara")
            }

            cameraError?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(key = "captured_photo") {
                    capturedPhoto?.let { photo ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Foto capturada",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Image(
                                    bitmap = photo.asImageBitmap(),
                                    contentDescription = "Foto tomada con la cámara",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 280.dp),
                                    contentScale = ContentScale.Fit
                                )
                                TextButton(
                                    onClick = { capturedPhoto = null },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Quitar foto")
                                }
                            }
                        }
                    }
                }

                item(key = "laws_heading") {
                    Text(
                        "Legislación Disponible:",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

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

