package com.example.movilaw.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_notes")
data class StudyNote(
    @PrimaryKey val noteId: String,
    val referencedLawId: String,
    val noteContent: String,
    val creationDate: Long
)
