package ru.vafeen.presentation.ui.feature.characters_screen

/**
 * Represents user intents (actions) for the Characters screen.
 */
internal sealed class CharactersIntent {

    /**
     * Intent to trigger a refresh action.
     */
    data object Refresh : CharactersIntent()
    data class ClickToCharacter(val id: Int) : CharactersIntent()
}
