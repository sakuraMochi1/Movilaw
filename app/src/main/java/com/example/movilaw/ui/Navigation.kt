package com.example.movilaw.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilaw.native.NotificationHelper
import com.example.movilaw.ui.screens.*
import com.example.movilaw.viewmodel.AuthViewModel
import com.example.movilaw.viewmodel.MainViewModel


sealed class Screen(val route: String) {
    object SessionCheck : Screen("session_check")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object Contacto : Screen("contacto_screen")
    object MiEspacio : Screen("mi_espacio_screen")
    object Profile : Screen("profile_screen")
}

@Composable
fun MovilawNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    notificationHelper: NotificationHelper
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SessionCheck.route
    ) {

        composable(Screen.SessionCheck.route) {
            val checkComplete by authViewModel.sessionCheckComplete.collectAsState()
            val hasActiveSession by authViewModel.hasActiveSession.collectAsState()

            LaunchedEffect(checkComplete, hasActiveSession) {
                if (checkComplete) {
                    val destination = if (hasActiveSession) Screen.Home.route else Screen.Login.route
                    navController.navigate(destination) {
                        popUpTo(Screen.SessionCheck.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                mainViewModel = mainViewModel,
                notificationHelper = notificationHelper,
                onNavigateToContacto = { navController.navigate(Screen.Contacto.route) },
                onNavigateToMiEspacio = { navController.navigate(Screen.MiEspacio.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }
        composable(Screen.Contacto.route) {
            ContactoScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.MiEspacio.route) {
            MiEspacioScreen(
                mainViewModel = mainViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                mainViewModel = mainViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
