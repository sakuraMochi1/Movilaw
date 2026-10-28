package com.example.movilaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.movilaw.data.MovilawDatabase
import com.example.movilaw.native.NotificationHelper
import com.example.movilaw.repository.LawRepository
import com.example.movilaw.ui.MovilawNavigation
import com.example.movilaw.ui.theme.LawNavyPrimary
import com.example.movilaw.viewmodel.AuthViewModel
import com.example.movilaw.viewmodel.MainViewModel
import com.example.movilaw.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de la persistencia (Room) y el repositorio
        val database = MovilawDatabase.getDatabase(applicationContext)
        val repository = LawRepository(database.userDao(), database.studyNoteDao())
        val factory = ViewModelFactory(repository)

        val authViewModel = factory.create(AuthViewModel::class.java)
        val mainViewModel = factory.create(MainViewModel::class.java)

        // Helper de recurso nativo
        val notificationHelper = NotificationHelper(applicationContext)

        setContent {
            val navController = rememberNavController()
            MovilawNavigation(
                navController = navController,
                authViewModel = authViewModel,
                mainViewModel = mainViewModel,
                notificationHelper = notificationHelper
            )
        }
    }
}