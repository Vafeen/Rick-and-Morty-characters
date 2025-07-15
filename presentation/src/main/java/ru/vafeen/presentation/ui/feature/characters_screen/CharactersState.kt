package ru.vafeen.presentation.ui.feature.characters_screen

import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersState

/**
 * Represents the UI state for the Characters screen.
 *
 * @property filtersState The current state of filters applied to the character list.
 * @property isFilterBottomSheetVisible Flag indicating whether the filters bottom sheet is visible.
 */
data class CharactersState(
    val filtersState: FiltersState = FiltersState(),
    val isFilterBottomSheetVisible: Boolean = false,
    val favourites: List<Int> = listOf(),
    val dataIsEmpty: Boolean = true,
)
