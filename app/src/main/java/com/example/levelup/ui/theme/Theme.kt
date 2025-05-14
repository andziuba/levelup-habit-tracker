package com.example.levelup.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DeepPurple,
    secondary = LightPurple,
    tertiary = NeonMagenta,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = LightText,
    onSecondary = DarkText,
    onTertiary = DarkText,
    onBackground = LightText,
    onSurface = LightText,
    error = CrimsonRed,
    onError = DarkText
)

private val LightColorScheme = lightColorScheme(
    primary = DeepPurple,
    secondary = LightPurple,
    tertiary = NeonMagenta,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onPrimary = LightText,
    onSecondary = DarkText,
    onTertiary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText,
    error = CrimsonRed,
    onError = Color.White
)


@Composable
fun LevelUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}