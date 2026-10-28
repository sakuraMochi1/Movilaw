package com.example.movilaw.model

data class StudyNote(
    val noteId: String,
    val relatedResourceId: String,
    val noteContent: String,
    val creationDate: Long,
    val isConceptMapPrompt: Boolean = false
)
