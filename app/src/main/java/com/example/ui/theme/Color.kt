package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// Serene Manuscript Design System Colors
val Primary = Color(0xFF003329)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF114B3E)
val OnPrimaryContainer = Color(0xFF83BAA9)
val PrimaryFixed = Color(0xFFB6EEDC)
val PrimaryFixedDim = Color(0xFF9AD2C0)

val Secondary = Color(0xFF7B580B)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFFCCD78)
val OnSecondaryContainer = Color(0xFF775607)
val SecondaryFixed = Color(0xFFFFDEA7)
val SecondaryFixedDim = Color(0xFFEDC06C)

val Tertiary = Color(0xFF1C302A)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFF324640)
val OnTertiaryContainer = Color(0xFF9DB3AB)
val TertiaryFixed = Color(0xFFD1E8DF)
val TertiaryFixedDim = Color(0xFFB5CBC3)

val Surface = Color(0xFFFAF9F4)
val OnSurface = Color(0xFF1B1C19)
val SurfaceVariant = Color(0xFFE3E3DE)
val OnSurfaceVariant = Color(0xFF404945)

val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF5F4EF)
val SurfaceContainer = Color(0xFFEFEEE9)
val SurfaceContainerHigh = Color(0xFFE9E8E3)
val SurfaceContainerHighest = Color(0xFFE3E3DE)

val Outline = Color(0xFF707975)
val OutlineVariant = Color(0xFFBFC9C4)

val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)

// Gold and Emerald specific accents
val GoldAccent = Color(0xFFC59B4B)
val GoldLight = Color(0xFFFDF9EE)
val EmeraldAccent = Color(0xFF16654A)
val EmeraldLight = Color(0xFFEAF4F0)

// Material 3 ColorScheme Extensions for Fixed/Dim/Accent Palette
val ColorScheme.primaryFixed: Color get() = PrimaryFixed
val ColorScheme.primaryFixedDim: Color get() = PrimaryFixedDim
val ColorScheme.secondaryFixed: Color get() = SecondaryFixed
val ColorScheme.secondaryFixedDim: Color get() = SecondaryFixedDim
val ColorScheme.tertiaryFixed: Color get() = TertiaryFixed
val ColorScheme.tertiaryFixedDim: Color get() = TertiaryFixedDim
val ColorScheme.onSecondaryFixed: Color get() = Color(0xFF261A00)
val ColorScheme.onTertiaryFixed: Color get() = Color(0xFF00201A)

