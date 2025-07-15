package ru.vafeen.presentation.ui.feature.favourites_screen

import ru.vafeen.domain.model.Settings

/**
 * Represents the UI state of the Favourites screen.
 *
 * @property settings The application settings relevant to this screen.
 * @property dataIsEmpty Indicates whether the favourites list is empty.
 */
data class FavouritesState(
    val settings: Settings,
    val dataIsEmpty: Boolean = true
)
