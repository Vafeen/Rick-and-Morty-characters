package ru.vafeen.domain.model

import androidx.compose.ui.graphics.Color

/**
 * Data class representing application settings.
 *
 * @property yourCharacterId Optional ID of the user’s preferred or selected character.
 * @property lightThemeColor Optional [Color] used for light theme customization.
 * @property darkThemeColor Optional [Color] used for dark theme customization.
 */
data class Settings(
    val yourCharacterId: Int? = null,
    val lightThemeColor: Color? = null,
    val darkThemeColor: Color? = null,
)
