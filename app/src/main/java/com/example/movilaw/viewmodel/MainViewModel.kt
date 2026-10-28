package com.example.movilaw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilaw.model.ResourceItem
import com.example.movilaw.model.StudyNote
import com.example.movilaw.model.User
import com.example.movilaw.repository.LawRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: LawRepository) : ViewModel() {

    // Usuario activo obtenido de Room convertido correctamente a StateFlow
    val activeUser: StateFlow<User?> = repository.activeUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Lista de leyes/artículos de lectura
    private val _lawsList = MutableStateFlow<List<ResourceItem>>(emptyList())
    val lawsList: StateFlow<List<ResourceItem>> = _lawsList.asStateFlow()

    // Lista de apuntes/notas personales del estudiante
    private val _notesList = MutableStateFlow<List<StudyNote>>(emptyList())
    val notesList: StateFlow<List<StudyNote>> = _notesList.asStateFlow()

    init {
        loadLegislation()
        loadStudyNotes()
    }

    private fun loadLegislation() {
        viewModelScope.launch {
            repository.getMockLegislation().collect { items ->
                _lawsList.value = items
            }
        }
    }

    private fun loadStudyNotes() {
        viewModelScope.launch {
            repository.getAllStudyNotes().collect { notes ->
                _notesList.value = notes
            }
        }
    }

    fun addStudyNote(resourceId: String, noteText: String) {
        if (noteText.isNotBlank()) {
            viewModelScope.launch {
                val newNote = StudyNote(
                    noteId = System.currentTimeMillis().toString(),
                    referencedLawId = resourceId,
                    noteContent = noteText,
                    creationDate = System.currentTimeMillis()
                )
                repository.saveStudyNote(newNote)
            }
        }
    }

    fun removeStudyNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteStudyNote(noteId)
        }
    }

    fun updateTextSizePreference(newSize: Float) {
        viewModelScope.launch {
            repository.updateUserTextSize(newSize)
        }
    }
}