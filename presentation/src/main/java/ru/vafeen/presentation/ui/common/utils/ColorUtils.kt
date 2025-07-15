package ru.vafeen.presentation.ui.common.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import ru.vafeen.domain.model.Settings
import kotlin.random.Random

/**
 * Returns a color that provides good contrast against the receiver color.
 *
 * If the color is dark, returns [Color.White], otherwise returns [Color.Black].
 *
 * @receiver The color for which to find a suitable contrasting color.
 * @return A contrasting [Color] (white or black).
 */
fun Color.suitableColor(): Color = if (this.isDark()) Color.White else Color.Black

/**
 * Determines whether this color is considered dark based on its luminance.
 *
 * @receiver The color to evaluate.
 * @return True if the color's luminance is below 0.4, indicating darkness.
 */
fun Color.isDark(): Boolean = ColorUtils.calculateLuminance(this.toArgb()) < 0.4

/**
 * Gets the main theme color based on light or dark mode setting.
 *
 * @receiver The [Settings] containing theme colors.
 * @param isDark True if the current theme is dark, false otherwise.
 * @return The corresponding [Color] for the theme or null if not set.
 */
fun Settings.getMainColorForThisTheme(isDark: Boolean): Color? =
    if (isDark) darkThemeColor else lightThemeColor

/**
 * Generates a random opaque color.
 *
 * @return A [Color] with random red, green, and blue components, and full alpha.
 */
internal fun generateRandomColor(): Color = Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256),
    alpha = 255
)
