package dev.meller.routeledger.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
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

@Composable
fun RouteLedgerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // MVP держим в премиальной светлой палитре. Тёмная тема появится отдельным слоем без неона.
    MaterialTheme(
        colorScheme = LightColors,
        typography = LedgerTypography,
        shapes = LedgerShapes,
        content = content,
    )
}
