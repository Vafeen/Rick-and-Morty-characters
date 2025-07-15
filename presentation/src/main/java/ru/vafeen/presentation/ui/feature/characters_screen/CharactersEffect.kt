package ru.vafeen.presentation.ui.feature.characters_screen

/**
 * Represents side effects for the Characters screen.
 */
internal sealed class CharactersEffect {

    /**
     * Effect to trigger a refresh action.
     */
    data object Refresh : CharactersEffect()
}
