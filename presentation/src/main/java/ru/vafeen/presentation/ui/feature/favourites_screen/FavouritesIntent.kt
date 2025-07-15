package ru.vafeen.presentation.ui.feature.favourites_screen

/**
 * Represents user intents (actions) for the Characters screen.
 */
internal sealed class FavouritesIntent {

    /**
     * Intent to trigger a refresh action.
     */
    data object Refresh : FavouritesIntent()

    /**
     * Intent representing a click on a character item.
     *
     * @param id The ID of the clicked character.
     */
    data class ClickToCharacter(val id: Int) : FavouritesIntent()
    data class ChangeIsFavourite(val id: Int) : FavouritesIntent()
}
