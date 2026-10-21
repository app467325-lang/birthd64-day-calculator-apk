package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = Primary,
  onPrimary = OnPrimary,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = Secondary,
  onSecondary = OnSecondary,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  tertiary = Tertiary,
  onTertiary = OnTertiary,
  tertiaryContainer = TertiaryContainer,
  onTertiaryContainer = OnTertiaryContainer,
  background = Surface,
  onBackground = OnSurface,
  surface = Surface,
  onSurface = OnSurface,
  surfaceVariant = SurfaceVariant,
  onSurfaceVariant = OnSurfaceVariant,
  surfaceContainerLowest = SurfaceContainerLowest,
  surfaceContainerLow = SurfaceContainerLow,
  surfaceContainer = SurfaceContainer,
  surfaceContainerHigh = SurfaceContainerHigh,
  surfaceContainerHighest = SurfaceContainerHighest,
  outline = Outline,
  outlineVariant = OutlineVariant,
  error = Error,
  onError = OnError,
  errorContainer = ErrorContainer,
  onErrorContainer = OnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
  primary = PrimaryFixed,
  onPrimary = Color(0xFF002019),
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = SecondaryFixed,
  onSecondary = Color(0xFF271900),
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  tertiary = TertiaryFixed,
  onTertiary = Color(0xFF0B1F1A),
  tertiaryContainer = TertiaryContainer,
  onTertiaryContainer = OnTertiaryContainer,
  background = Color(0xFF141E1B),
  onBackground = Color(0xFFE3E3DE),
  surface = Color(0xFF141E1B),
  onSurface = Color(0xFFE3E3DE),
  surfaceVariant = Color(0xFF24332D),
  onSurfaceVariant = Color(0xFFBFC9C4),
  surfaceContainerLowest = Color(0xFF0F1815),
  surfaceContainerLow = Color(0xFF182420),
  surfaceContainer = Color(0xFF1D2A26),
  surfaceContainerHigh = Color(0xFF23322D),
  surfaceContainerHighest = Color(0xFF2A3A34),
  outline = Outline,
  outlineVariant = OutlineVariant
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Luxury dark liquid glass mode with deep blues & purples
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
