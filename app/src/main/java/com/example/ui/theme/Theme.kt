package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = DealPink,
    onPrimary = Color.White,
    primaryContainer = DealPinkContainer,
    onPrimaryContainer = DealPinkOnContainer,
    secondary = DealPurple,
    onSecondary = Color.White,
    secondaryContainer = DealPurpleContainer,
    onSecondaryContainer = DealPurpleDark,
    tertiary = DealGold,
    onTertiary = Color.White,
    tertiaryContainer = DealGoldContainer,
    onTertiaryContainer = DealGoldOnContainer,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6584),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF8C002D),
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = Color(0xFFD8B4FE),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF581C87),
    onSecondaryContainer = Color(0xFFF3E8FF),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    outline = Color(0xFF334155)
)

@Composable
fun DealDropzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
