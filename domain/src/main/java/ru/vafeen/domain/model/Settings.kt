package ru.vafeen.domain.model

import androidx.compose.ui.graphics.Color

data class Settings(
    val yourCharacterId: Int? = null,
    val lightThemeColor: Color? = null,
    val darkThemeColor: Color? = null,
)
