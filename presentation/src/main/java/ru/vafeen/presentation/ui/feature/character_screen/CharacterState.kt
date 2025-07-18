package ru.vafeen.presentation.ui.feature.character_screen

import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Settings

/**
 * Represents the UI state for the Character screen.
 *
 * @property settings The application settings relevant to this screen.
 * @property characterData The character data to display, or null if not yet loaded.
 * @property isLoading Indicates if the character data is currently being loaded.
 * @property isError Indicates if an error occurred while loading character data.
 */
internal data class CharacterState(
    val settings: Settings,
    val characterData: CharacterData? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)
