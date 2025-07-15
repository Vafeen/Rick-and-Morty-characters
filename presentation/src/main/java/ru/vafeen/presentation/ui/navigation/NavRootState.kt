package ru.vafeen.presentation.ui.navigation

import ru.vafeen.presentation.common.navigation.Screen

internal data class NavRootState(
    val startScreen: Screen,
    val currentScreen: Screen = startScreen,
    val isBottomBarVisible: Boolean = false,
//    val release: Release? = null,
//    val settings: Settings,
//    val isUpdateInProgress: Boolean = false,
//    val percentage: Float = 0f,
)