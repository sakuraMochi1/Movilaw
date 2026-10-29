package com.example.movilaw.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movilaw.model.StudyNote
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNote)

    @Query("SELECT * FROM study_notes ORDER BY creationDate DESC")
    fun getAllNotes(): Flow<List<StudyNote>>

    @Query("DELETE FROM study_notes WHERE noteId = :noteId")
    suspend fun deleteNoteById(noteId: String)
}