package ru.vafeen.presentation.ui.navigation

import androidx.navigation.NavHostController

/**
 * Represents side effects related to navigation actions in the NavRoot.
 */
internal sealed class NavRootEffect {
    /**
     * Effect to navigate to a specific screen using the provided navigation lambda.
     *
     * @param navigate A lambda that takes a [NavHostController] and performs navigation.
     */
    data class NavigateToScreen(val navigate: (NavHostController) -> Unit) : NavRootEffect()

    /**
     * Effect to clear the navigation back stack.
     */
    data object ClearBackStack : NavRootEffect()

    /**
     * Effect to navigate back in the navigation stack.
     */
    data object Back : NavRootEffect()
}
