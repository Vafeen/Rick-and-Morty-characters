package ru.vafeen.presentation.ui.feature.profile_screen

import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Settings

/**
 * Represents the UI state for the Profile screen.
 *
 * @property id The ID of the profile character, or null if not set.
 * @property settings The current app settings.
 * @property characterData The character data to display, or null if not yet loaded.
 * @property isLoading Indicates if the character data is currently being loaded.
 * @property isError Indicates if an error occurred while loading character data.
 */
internal data class ProfileState(
    val id: Int? = null,
    val settings: Settings,
    val characterData: CharacterData? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)
