package com.vaultx.ultrafilelocker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Cyberpunk Color Palette
val CyberPink = Color(0xFFFF0080)
val CyberCyan = Color(0xFF00FFFF)
val CyberPurple = Color(0xFF8000FF)
val CyberGreen = Color(0xFF00FF80)
val CyberYellow = Color(0xFFFFFF00)
val CyberOrange = Color(0xFFFF8000)

val NeonBlue = Color(0xFF0080FF)
val NeonPink = Color(0xFFFF0080)
val NeonGreen = Color(0xFF80FF00)
val NeonPurple = Color(0xFF8000FF)

val DarkBackground = Color(0xFF0A0A0A)
val DarkSurface = Color(0xFF1A1A1A)
val DarkSurfaceVariant = Color(0xFF2A2A2A)

val LightBackground = Color(0xFFF5F5F5)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF0F0F0)

// Dark Theme Colors
val CyberpunkDarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = DarkBackground,
    primaryContainer = CyberCyan.copy(alpha = 0.2f),
    onPrimaryContainer = CyberCyan,
    
    secondary = CyberPink,
    onSecondary = DarkBackground,
    secondaryContainer = CyberPink.copy(alpha = 0.2f),
    onSecondaryContainer = CyberPink,
    
    tertiary = CyberPurple,
    onTertiary = DarkBackground,
    tertiaryContainer = CyberPurple.copy(alpha = 0.2f),
    onTertiaryContainer = CyberPurple,
    
    background = DarkBackground,
    onBackground = Color.White,
    
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color.White.copy(alpha = 0.8f),
    
    error = Color(0xFFFF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFF4444).copy(alpha = 0.2f),
    onErrorContainer = Color(0xFFFF4444),
    
    outline = CyberCyan.copy(alpha = 0.5f),
    outlineVariant = CyberPink.copy(alpha = 0.3f)
)

// Light Theme Colors (for contrast)
val CyberpunkLightColorScheme = lightColorScheme(
    primary = NeonBlue,
    onPrimary = Color.White,
    primaryContainer = NeonBlue.copy(alpha = 0.1f),
    onPrimaryContainer = NeonBlue,
    
    secondary = NeonPink,
    onSecondary = Color.White,
    secondaryContainer = NeonPink.copy(alpha = 0.1f),
    onSecondaryContainer = NeonPink,
    
    tertiary = NeonPurple,
    onTertiary = Color.White,
    tertiaryContainer = NeonPurple.copy(alpha = 0.1f),
    onTertiaryContainer = NeonPurple,
    
    background = LightBackground,
    onBackground = Color.Black,
    
    surface = LightSurface,
    onSurface = Color.Black,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color.Black.copy(alpha = 0.8f),
    
    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFD32F2F).copy(alpha = 0.1f),
    onErrorContainer = Color(0xFFD32F2F),
    
    outline = NeonBlue.copy(alpha = 0.5f),
    outlineVariant = NeonPink.copy(alpha = 0.3f)
)

// Matrix Theme Colors
val MatrixGreen = Color(0xFF00FF41)
val MatrixDarkGreen = Color(0xFF008F11)
val MatrixBlack = Color(0xFF000000)
val MatrixDarkGray = Color(0xFF0D1117)

val MatrixColorScheme = darkColorScheme(
    primary = MatrixGreen,
    onPrimary = MatrixBlack,
    primaryContainer = MatrixGreen.copy(alpha = 0.2f),
    onPrimaryContainer = MatrixGreen,
    
    secondary = MatrixDarkGreen,
    onSecondary = MatrixBlack,
    secondaryContainer = MatrixDarkGreen.copy(alpha = 0.2f),
    onSecondaryContainer = MatrixDarkGreen,
    
    tertiary = MatrixGreen,
    onTertiary = MatrixBlack,
    tertiaryContainer = MatrixGreen.copy(alpha = 0.2f),
    onTertiaryContainer = MatrixGreen,
    
    background = MatrixBlack,
    onBackground = MatrixGreen,
    
    surface = MatrixDarkGray,
    onSurface = MatrixGreen,
    surfaceVariant = MatrixDarkGray,
    onSurfaceVariant = MatrixGreen.copy(alpha = 0.8f),
    
    error = Color(0xFFFF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFF4444).copy(alpha = 0.2f),
    onErrorContainer = Color(0xFFFF4444),
    
    outline = MatrixGreen.copy(alpha = 0.5f),
    outlineVariant = MatrixDarkGreen.copy(alpha = 0.3f)
)

// Neon Theme Colors
val NeonColorScheme = darkColorScheme(
    primary = NeonBlue,
    onPrimary = DarkBackground,
    primaryContainer = NeonBlue.copy(alpha = 0.2f),
    onPrimaryContainer = NeonBlue,
    
    secondary = NeonPink,
    onSecondary = DarkBackground,
    secondaryContainer = NeonPink.copy(alpha = 0.2f),
    onSecondaryContainer = NeonPink,
    
    tertiary = NeonGreen,
    onTertiary = DarkBackground,
    tertiaryContainer = NeonGreen.copy(alpha = 0.2f),
    onTertiaryContainer = NeonGreen,
    
    background = DarkBackground,
    onBackground = Color.White,
    
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color.White.copy(alpha = 0.8f),
    
    error = Color(0xFFFF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFF4444).copy(alpha = 0.2f),
    onErrorContainer = Color(0xFFFF4444),
    
    outline = NeonBlue.copy(alpha = 0.5f),
    outlineVariant = NeonPink.copy(alpha = 0.3f)
)

