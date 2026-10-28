package com.example.movilaw.model

data class Contact(
    val contactId: String,
    val fullName: String,
    val email: String,
    val academicRole: String,
    val isOnline: Boolean = false
)