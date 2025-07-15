package ru.vafeen.presentation.common.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

/**
 * Represents the various navigation destinations (screens) in the application.
 */
internal sealed interface Screen {

    /** The main screen listing all characters. */
    @Serializable
    data object Characters : Screen

    /** Screen that represents bottom bar container or grouping. */
    @Serializable
    data object BottomBarScreens : Screen

    /** The user's profile screen. */
    @Serializable
    data object Profile : Screen

    /**
     * Screen showing details of a character.
     *
     * @property id The unique identifier of the character to display.
     */
    @Serializable
    data class Character(val id: Int) : Screen

    /** The favourites screen showing user's liked characters. */
    @Serializable
    data object Favourites : Screen

    /** The settings screen. */
    @Serializable
    data object Settings : Screen
}

/**
 * List of screens that are accessible via the bottom bar navigation.
 */
internal val screenWithBottomBar = listOf(
    Screen.Characters,
    Screen.Profile,
    Screen.BottomBarScreens,
    Screen.Favourites,
    Screen.Settings
)

/**
 * Helper function to convert a [NavBackStackEntry] into a [Screen] instance,
 * based on the current route.
 *
 * This function matches routes by their fully qualified class names and parses parameters if necessary.
 *
 * @param navBackStackEntry The navigation back stack entry to convert.
 * @return Corresponding [Screen] instance or null if no match found.
 */
internal fun getScreenFromRoute(navBackStackEntry: NavBackStackEntry): Screen? {
    val route = navBackStackEntry.destination.route ?: return null
    return when {
        route == Screen.Characters::class.qualifiedName -> Screen.Characters
        route == Screen.BottomBarScreens::class.qualifiedName -> Screen.BottomBarScreens
        route == Screen.Settings::class.qualifiedName -> Screen.Settings
        route == Screen.Favourites::class.qualifiedName -> Screen.Favourites
        route == Screen.Profile::class.qualifiedName -> Screen.Profile
        route.startsWith("${Screen.Character::class.qualifiedName}") ->
            navBackStackEntry.toRoute<Screen.Character>()
        else -> null
    }
}
