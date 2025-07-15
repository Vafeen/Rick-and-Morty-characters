package ru.vafeen.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Data class representing the color palette for the app theme.
 *
 * @property mainColor Primary color used in the app.
 * @property background Background color of the UI.
 * @property text Color used for text content.
 * @property buttonColor Color used for buttons.
 * @property error Color used to indicate errors.
 */
internal data class AppThemeColors(
    val mainColor: Color,
    val background: Color,
    val text: Color,
    val buttonColor: Color,
    val error: Color,
)

/**
 * Light color palette for the app theme.
 */
private val basePalette = AppThemeColors(
    mainColor = Color(0xFFECEA0E),
    background = Color.White,
    text = Color.Black,
    error = Color.Red,
    buttonColor = Color(0xFFF9F9F9)
)

/**
 * Dark color palette for the app theme, based on [basePalette] with overrides.
 */
private val baseDarkPalette = basePalette.copy(
    background = Color.Black,
    text = Color.White,
    buttonColor = Color(0xFF2D2D31)
)

/**
 * Composable function that applies the main theme to the app's UI.
 *
 * It selects either the light or dark color palette based on the [darkTheme] parameter,
 * which defaults to the system setting.
 *
 * @param darkTheme Whether to use the dark theme colors.
 * @param content The composable content to which the theme will be applied.
 */
@Composable
internal fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        baseDarkPalette
    } else {
        basePalette
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}

/**
 * Object to access the current app theme colors from the composition.
 */
internal object AppTheme {
    /**
     * The current colors used by the app theme.
     */
    val colors: AppThemeColors
        @Composable
        get() = LocalColors.current
}

/**
 * CompositionLocal holding the current [AppThemeColors].
 *
 * Throws an error if accessed outside of a [MainTheme] composable.
 */
private val LocalColors = staticCompositionLocalOf<AppThemeColors> {
    error("Composition error: AppThemeColors not provided")
}
