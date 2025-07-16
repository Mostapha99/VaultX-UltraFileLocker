package com.vaultx.ultrafilelocker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vaultx.ultrafilelocker.ui.screens.SplashScreen
import com.vaultx.ultrafilelocker.ui.screens.AuthScreen
import com.vaultx.ultrafilelocker.ui.screens.MainScreen

sealed class VaultXDestination(val route: String) {
    object Splash : VaultXDestination("splash")
    object Auth : VaultXDestination("auth")
    object Main : VaultXDestination("main")
    object VaultGallery : VaultXDestination("vault_gallery")
    object MediaPlayer : VaultXDestination("media_player")
    object Settings : VaultXDestination("settings")
}

@Composable
fun VaultXNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = VaultXDestination.Splash.route,
        modifier = modifier
    ) {
        composable(VaultXDestination.Splash.route) {
            SplashScreen(
                onNavigateToAuth = {
                    navController.navigate(VaultXDestination.Auth.route) {
                        popUpTo(VaultXDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(VaultXDestination.Main.route) {
                        popUpTo(VaultXDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(VaultXDestination.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(VaultXDestination.Main.route) {
                        popUpTo(VaultXDestination.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(VaultXDestination.Main.route) {
            MainScreen(
                onNavigateToGallery = {
                    navController.navigate(VaultXDestination.VaultGallery.route)
                },
                onNavigateToSettings = {
                    navController.navigate(VaultXDestination.Settings.route)
                }
            )
        }
        
        composable(VaultXDestination.VaultGallery.route) {
            // VaultGalleryScreen will be implemented in features module
        }
        
        composable(VaultXDestination.MediaPlayer.route) {
            // MediaPlayerScreen will be implemented in features module
        }
        
        composable(VaultXDestination.Settings.route) {
            // SettingsScreen will be implemented in features module
        }
    }
}

