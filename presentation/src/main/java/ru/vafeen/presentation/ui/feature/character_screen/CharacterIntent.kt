package ru.vafeen.presentation.ui.feature.character_screen

/**
 * Represents user intents/actions for the Character screen.
 */
internal sealed class CharacterIntent {
    /**
     * Intent to fetch character data.
     */
    data object FetchData : CharacterIntent()
}
