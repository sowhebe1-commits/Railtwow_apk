package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = RailBluePrimary,
    secondary = RailBlueDark,
    background = Color(0xFF111827),
    surface = Color(0xFF1F2937),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RailBluePrimary,
    secondary = RailBlueDark,
    background = RailBackground,
    surface = RailSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = RailTextPrimary,
    onSurface = RailTextPrimary,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
