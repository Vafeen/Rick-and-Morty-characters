package ru.vafeen.presentation.ui.feature.favourites_screen

/**
 * Represents user intents (actions) for the Favourites screen.
 */
internal sealed class FavouritesIntent {

    /**
     * Intent to trigger a refresh action, e.g., reloading the favourites list.
     */
    data object Refresh : FavouritesIntent()

    /**
     * Intent representing a click on a favourite character item.
     *
     * @param id The ID of the clicked character.
     */
    data class ClickToCharacter(val id: Int) : FavouritesIntent()

    /**
     * Intent to change the favourite status of a character.
     *
     * @param id The ID of the character to toggle favourite status.
     */
    data class ChangeIsFavourite(val id: Int) : FavouritesIntent()

    /**
     * Intent indicating whether the favourites data is empty.
     *
     * @param isEmpty True if the favourites list is empty, false otherwise.
     */
    data class IsDataEmpty(val isEmpty: Boolean) : FavouritesIntent()
    data class SetIsMyCharacter(val id: Int) : FavouritesIntent()
}
