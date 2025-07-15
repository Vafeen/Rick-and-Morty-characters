package ru.vafeen.presentation.common.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

internal sealed interface Screen {
    @Serializable
    data object Characters : Screen

    @Serializable
    data object BottomBarScreens : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data class Character(val id: Int) : Screen

    @Serializable
    data object Favourites : Screen
}

internal val screenWithBottomBar = listOf(
    Screen.Characters, Screen.Profile, Screen.BottomBarScreens, Screen.Favourites
)

internal fun getScreenFromRoute(navBackStackEntry: NavBackStackEntry): Screen? {
    val route = navBackStackEntry.destination.route ?: return null
    return when {
        route == Screen.Characters::class.qualifiedName -> Screen.Characters
        route == Screen.BottomBarScreens::class.qualifiedName -> Screen.BottomBarScreens
        route == Screen.Favourites::class.qualifiedName -> Screen.Favourites
        route == Screen.Profile::class.qualifiedName -> Screen.Profile
        route.startsWith("${Screen.Character::class.qualifiedName}") -> navBackStackEntry.toRoute<Screen.Character>()
        else -> null
    }
}