package com.example.movilaw.repository

import com.example.movilaw.data.StudyNoteDao
import com.example.movilaw.data.UserDao
import com.example.movilaw.model.ResourceItem
import com.example.movilaw.model.StudyNote
import com.example.movilaw.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio central que gestiona la comunicación entre los DAOs (base de datos local)
 * y la capa de presentación (ViewModels).
 */
class LawRepository(
    private val userDao: UserDao,
    private val studyNoteDao: StudyNoteDao
) {

    // ==========================================
    // GESTIÓN DE SESIÓN Y USUARIO
    // ==========================================

    // Expone el estado del usuario activo en tiempo real mediante Flow
    val activeUser: Flow<User?> = userDao.getActiveUser()

    suspend fun saveSession(email: String, name: String, profileType: String) {
        // Cierra sesiones previas por seguridad antes de crear una nueva
        userDao.logoutAllUsers()
        val newUser = User(email = email, name = name, profileType = profileType, isLoggedIn = true)
        userDao.insertUser(newUser)
    }

    suspend fun clearSession() {
        userDao.logoutAllUsers()
    }

    suspend fun updateUserTextSize(newSize: Float) {
        userDao.updateTextSize(newSize)
    }

    // ==========================================
    // GESTIÓN DE NOTAS DE ESTUDIO
    // ==========================================

    // Obtiene todas las notas personales guardadas en la base de datos local
    fun getAllStudyNotes(): Flow<List<StudyNote>> {
        return studyNoteDao.getAllNotes()
    }

    suspend fun saveStudyNote(note: StudyNote) {
        studyNoteDao.insertNote(note)
    }

    suspend fun deleteStudyNote(noteId: String) {
        studyNoteDao.deleteNoteById(noteId)
    }

    // ==========================================
    // GESTIÓN DE LEGISLACIÓN (MOCKS PARA EL MVP)
    // ==========================================

    /**
     * Simula la obtención de un catálogo de normativas actualizadas.
     * El caso permite el uso de datos de prueba o mocks para representar la consulta jurídica.
     */
    fun getMockLegislation(): Flow<List<ResourceItem>> = flow {
        val mockData = listOf(
            ResourceItem(
                id = "ley_1",
                referenceTitle = "Artículo 1 - Código Civil",
                legalCategory = "Derecho Civil",
                content = "La ley es una declaración de la voluntad soberana que, manifestada en la forma prescrita por la Constitución, manda, prohíbe o permite.",
                lastModificationYear = 2000,
                isUpdated = true
            ),
            ResourceItem(
                id = "ley_2",
                referenceTitle = "Artículo 1 - Código Penal",
                legalCategory = "Derecho Penal",
                content = "Es delito toda acción u omisión voluntaria penada por la ley.",
                lastModificationYear = 2023,
                isUpdated = true
            )
        )
        emit(mockData)
    }
}