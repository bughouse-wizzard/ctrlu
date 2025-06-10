// ui/theme/Theme.kt
package com.example.ctrlu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ScarletColorScheme = darkColorScheme(
    primary = ScarletRed,
    secondary = DarkPurple,
    tertiary = Fuchsia,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = OnPrimary,
    onSecondary = OnPrimary,
    onTertiary = OnPrimary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant
)

@Composable
fun CtrluTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = ScarletRed,
        surface = DarkSurface,
        background = DarkBackground,
        onPrimary = OnPrimary,
        onSurface = OnSurface,
        onSurfaceVariant = OnSurfaceVariant,
        onBackground = OnBackground,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}