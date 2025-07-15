package ru.vafeen.presentation.ui.navigation

import ru.vafeen.presentation.common.navigation.Screen

/**
 * Represents user intents or actions that can be handled by the NavRootViewModel.
 */
internal sealed class NavRootIntent {

    /**
     * Intent to clear the navigation back stack.
     */
    data object ClearBackStack : NavRootIntent()

    /**
     * Intent to navigate to a screen represented by a bottom bar item.
     *
     * @param screen The target screen to navigate to.
     */
    data class NavigateToBottomBarScreen(val screen: Screen) : NavRootIntent()

    /**
     * Intent to navigate to a specific screen.
     *
     * @param screen The target screen to navigate to.
     */
    data class NavigateToScreen(val screen: Screen) : NavRootIntent()

    /**
     * Intent to update the currently active screen.
     *
     * @param screen The screen that is currently active.
     */
    data class UpdateCurrentScreen(val screen: Screen) : NavRootIntent()

    /**
     * Intent to change the visibility of the bottom navigation bar.
     *
     * @param isVisible True if the bottom bar should be visible, false otherwise.
     */
    data class ChangeBottomBarVisible(val isVisible: Boolean) : NavRootIntent()

    /**
     * Intent to navigate back in the navigation stack.
     */
    data object Back : NavRootIntent()
}
