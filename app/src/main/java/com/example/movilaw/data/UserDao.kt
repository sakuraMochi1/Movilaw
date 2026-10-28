package com.example.movilaw.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movilaw.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getActiveUser(): Flow<User?>

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()

    @Query("UPDATE users SET preferredTextSize = :newSize WHERE isLoggedIn = 1")
    suspend fun updateTextSize(newSize: Float)
}