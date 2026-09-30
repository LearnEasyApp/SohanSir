package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = LightSurface,
    primaryContainer = BlueContainer,
    onPrimaryContainer = OnBlueContainer,
    secondary = AmberAccent,
    onSecondary = LightSurface,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = OnAmberContainer,
    tertiary = GreenSuccess,
    onTertiary = LightSurface,
    tertiaryContainer = GreenContainer,
    onTertiaryContainer = OnGreenContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    error = RedError,
    onError = LightSurface,
    errorContainer = RedContainer,
    onErrorContainer = OnRedContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkBluePrimary,
    onPrimary = DarkBackground,
    primaryContainer = DarkBlueContainer,
    onPrimaryContainer = DarkOnBackground,
    secondary = DarkAmberAccent,
    onSecondary = DarkBackground,
    secondaryContainer = AmberDark,
    onSecondaryContainer = AmberContainer,
    tertiary = GreenSuccess,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    error = RedError,
    onError = DarkBackground,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)

@Composable
fun LearnEasyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BengaliTypography,
        content = content
    )
}
