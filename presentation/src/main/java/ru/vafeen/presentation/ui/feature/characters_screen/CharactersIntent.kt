package ru.vafeen.presentation.ui.feature.characters_screen

import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersState

/**
 * Represents user intents (actions) for the Characters screen.
 */
internal sealed class CharactersIntent {

    /**
     * Intent to trigger a refresh action.
     */
    data object Refresh : CharactersIntent()

    /**
     * Intent representing a click on a character item.
     *
     * @param id The ID of the clicked character.
     */
    data class ClickToCharacter(val id: Int) : CharactersIntent()

    /**
     * Intent to change the visibility of the filters bottom sheet.
     *
     * @param isVisible True if filters should be shown, false otherwise.
     */
    data class ChangeFilterVisibility(val isVisible: Boolean) : CharactersIntent()

    /**
     * Intent to apply the given filters to the character list.
     *
     * @param filtersState The state representing currently selected filters.
     */
    data class ApplyFilters(val filtersState: FiltersState) : CharactersIntent()
    data class ChangeIsFavourite(val id: Int) : CharactersIntent()
}
