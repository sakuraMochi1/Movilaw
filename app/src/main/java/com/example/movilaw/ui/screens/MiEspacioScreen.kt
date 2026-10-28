package com.example.movilaw.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movilaw.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiEspacioScreen(
    mainViewModel: MainViewModel,
    onBack: () -> Unit
) {
    val notes by mainViewModel.notesList.collectAsState()
    var newNoteText by remember { mutableStateOf("") }
    var selectedNoteIdForDelete by remember { mutableStateOf<String?>(null) }

    // MODAL REQUERIDO (AlertDialog)
    if (selectedNoteIdForDelete != null) {
        AlertDialog(
            onDismissRequest = { selectedNoteIdForDelete = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Deseas eliminar esta nota de estudio?") },
            confirmButton = {
                TextButton(onClick = {
                    selectedNoteIdForDelete?.let { mainViewModel.removeStudyNote(it) }
                    selectedNoteIdForDelete = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedNoteIdForDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Espacio - Apuntes") },
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
            OutlinedTextField(
                value = newNoteText,
                onValueChange = { newNoteText = it },
                label = { Text("Nueva nota de estudio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    mainViewModel.addStudyNote("ley_1", newNoteText)
                    newNoteText = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Nota")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notes) { note ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(note.noteContent, modifier = Modifier.weight(1f))
                            IconButton(onClick = { selectedNoteIdForDelete = note.noteId }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}

