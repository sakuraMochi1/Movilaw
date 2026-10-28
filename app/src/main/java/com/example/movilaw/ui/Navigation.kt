package com.example.movilaw.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilaw.native.NotificationHelper
import com.example.movilaw.ui.screens.*
import com.example.movilaw.viewmodel.AuthViewModel
import com.example.movilaw.viewmodel.MainViewModel

// Definición de rutas únicas para el flujo de navegación
sealed class Screen(val route: String) {
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
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
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
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
