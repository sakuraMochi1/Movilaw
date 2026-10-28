package com.example.movilaw.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val name: String,
    val profileType: String,
    val preferredTextSize: Float = 16f,
    val isLoggedIn: Boolean = false
)
