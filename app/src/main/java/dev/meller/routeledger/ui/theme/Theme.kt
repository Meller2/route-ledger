package dev.meller.routeledger.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = LedgerInk,
    onPrimary = LedgerPaper,
    secondary = LedgerGold,
    onSecondary = LedgerInk,
    tertiary = LedgerSage,
    background = LedgerIvory,
    onBackground = LedgerInk,
    surface = LedgerPaper,
    onSurface = LedgerInk,
    surfaceVariant = Color(0xFFF0E7DB),
    onSurfaceVariant = LedgerMutedInk,
    outline = LedgerLine,
)

private val DarkColors = darkColorScheme(
    primary = LedgerGold,
    onPrimary = LedgerInk,
    secondary = LedgerSage,
    onSecondary = LedgerInk,
    tertiary = LedgerClay,
    background = Color(0xFF12110F),
    onBackground = Color(0xFFF2E8DA),
    surface = Color(0xFF1C1916),
    onSurface = Color(0xFFF2E8DA),
    surfaceVariant = Color(0xFF2B261F),
    onSurfaceVariant = Color(0xFFCFC2B2),
    outline = Color(0xFF40382F),
)

@Composable
fun RouteLedgerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = LedgerTypography,
        shapes = LedgerShapes,
        content = content,
    )
}
