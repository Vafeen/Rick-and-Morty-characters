package ru.vafeen.presentation.ui.navigation

import ru.vafeen.presentation.common.navigation.Screen

internal sealed class NavRootIntent {
    data object ClearBackStack : NavRootIntent()
    data class NavigateToBottomBarScreen(val screen: Screen) : NavRootIntent()
    data class NavigateToScreen(val screen: Screen) : NavRootIntent()
    data class UpdateCurrentScreen(val screen: Screen) : NavRootIntent()
    data class ChangeBottomBarVisible(val isVisible: Boolean) : NavRootIntent()
    data object Back : NavRootIntent()
//    data object CheckUpdates : NavRootIntent()
//    data object Update : NavRootIntent()
}