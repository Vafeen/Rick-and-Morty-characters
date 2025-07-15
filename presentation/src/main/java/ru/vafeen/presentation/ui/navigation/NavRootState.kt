package ru.vafeen.presentation.ui.navigation

import ru.vafeen.domain.model.Settings
import ru.vafeen.presentation.common.navigation.Screen

/**
 * Represents the UI state of the NavRoot component.
 *
 * @property startScreen The initial screen to be displayed when the app starts.
 * @property currentScreen The currently active screen.
 * @property isBottomBarVisible Indicates whether the bottom navigation bar is visible.
 */
internal data class NavRootState(
    val settings: Settings,
    val startScreen: Screen,
    val currentScreen: Screen = startScreen,
    val isBottomBarVisible: Boolean = false,
    val isMyCharacterChosen: Boolean = false
)
