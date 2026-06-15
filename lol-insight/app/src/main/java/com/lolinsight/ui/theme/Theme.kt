package com.lolinsight.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LolGold,
    onPrimary = LolDark,
    primaryContainer = LolGoldDark,
    onPrimaryContainer = LolGoldLight,
    secondary = AllyBlue,
    onSecondary = LolDark,
    secondaryContainer = AllyBlueDark,
    onSecondaryContainer = AllyBlueLight,
    tertiary = EnemyRed,
    onTertiary = LolDark,
    tertiaryContainer = EnemyRedDark,
    onTertiaryContainer = EnemyRedLight,
    error = EnemyRed,
    onError = LolDark,
    errorContainer = EnemyRedDark,
    onErrorContainer = EnemyRedLight,
    background = LolDark,
    onBackground = TextPrimary,
    surface = LolSurface,
    onSurface = TextPrimary,
    surfaceVariant = LolSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = LolBorder,
    outlineVariant = CardBorder,
    scrim = LolDarker,
    inverseSurface = TextPrimary,
    inverseOnSurface = LolDark,
    inversePrimary = LolGoldDark
)

@Composable
fun LoLInsightTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = LolDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
