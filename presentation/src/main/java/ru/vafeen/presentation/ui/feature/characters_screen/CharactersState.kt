package ru.vafeen.presentation.ui.feature.characters_screen

import ru.vafeen.domain.model.Settings
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.Filters

/**
 * Represents the UI state for the Characters screen.
 *
 * @property settings The current app settings relevant to this screen.
 * @property filters The current state of filters applied to the character list.
 * @property isFilterBottomSheetVisible Flag indicating whether the filters bottom sheet is visible.
 * @property favourites List of character IDs that are marked as favourites.
 * @property dataIsEmpty Flag indicating whether the current character data set is empty.
 */
data class CharactersState(
    val settings: Settings,
    val filters: Filters = Filters(),
    val isFilterBottomSheetVisible: Boolean = false,
    val favourites: List<Int> = listOf(),
    val dataIsEmpty: Boolean = true,
)
