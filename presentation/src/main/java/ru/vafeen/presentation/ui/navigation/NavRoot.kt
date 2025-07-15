package ru.vafeen.presentation.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.bottom_bar.BottomBarItem
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.common.navigation.getScreenFromRoute
import ru.vafeen.presentation.ui.common.components.BottomBar
import ru.vafeen.presentation.ui.feature.character_screen.CharacterScreen
import ru.vafeen.presentation.ui.feature.character_screen.ProfileScreen
import ru.vafeen.presentation.ui.feature.characters_screen.CharactersScreen
import ru.vafeen.presentation.ui.feature.favourites_screen.FavouritesScreen
import ru.vafeen.presentation.ui.feature.settings_screen.SettingsScreen
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.MainTheme

/**
 * Root navigation composable that sets up the navigation graph and UI scaffolding.
 *
 * This composable manages the navigation controller, listens to navigation state changes,
 * handles navigation effects from the ViewModel, and displays the bottom navigation bar
 * when appropriate.
 *
 * It uses [MainTheme] for theming and provides animated transitions between screens.
 */
@Composable
internal fun NavRoot() {
    MainTheme {
        val navController = rememberNavController()
        val viewModel: NavRootViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(null) {
            navController.currentBackStackEntryFlow.collect {
                val currentScreen = getScreenFromRoute(it) ?: return@collect
                viewModel.handleIntent(NavRootIntent.UpdateCurrentScreen(currentScreen))
            }
        }

        LaunchedEffect(null) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    NavRootEffect.Back -> navController.popBackStack()
                    is NavRootEffect.NavigateToScreen -> effect.navigate(navController)
                    NavRootEffect.ClearBackStack -> navController.navigateUp()
                }
            }
        }

        Scaffold(
            containerColor = AppTheme.colors.background,
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val bottomBarScreens: List<BottomBarItem> by rememberUpdatedState(
                    listOf(
                        BottomBarItem(
                            screen = Screen.Characters,
                            icon = painterResource(R.drawable.characters),
                            contentDescription = stringResource(R.string.characters)
                        ),
                        BottomBarItem(
                            screen = Screen.Favourites,
                            icon = painterResource(R.drawable.favorite_full),
                            contentDescription = stringResource(R.string.favourites_screen)
                        ),
                        BottomBarItem(
                            screen = Screen.Settings,
                            icon = painterResource(R.drawable.settings),
                            contentDescription = stringResource(R.string.settings_screen)
                        )
                    ).let {
                        if (state.settings.yourCharacterId != null) it.plus(
                            BottomBarItem(
                                screen = Screen.Profile,
                                icon = painterResource(R.drawable.profile),
                                contentDescription = stringResource(R.string.profile_screen)
                            )
                        ) else it
                    })

                if (state.isBottomBarVisible) {
                    BottomBar(
                        containerColor = AppTheme.colors.mainColor,
                        currentScreen = state.currentScreen,
                        screens = bottomBarScreens,
                        navigateTo = { screen ->
                            viewModel.handleIntent(NavRootIntent.NavigateToBottomBarScreen(screen))
                        }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                val tween = tween<Float>(durationMillis = 0)
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    startDestination = state.startScreen,
                    enterTransition = { fadeIn(animationSpec = tween) },
                    exitTransition = { fadeOut(animationSpec = tween) },
                    popEnterTransition = { fadeIn(animationSpec = tween) },
                    popExitTransition = { fadeOut(animationSpec = tween) },
                ) {
                    composable<Screen.Character> {
                        val character = it.toRoute<Screen.Character>()
                        CharacterScreen(
                            character = character,
                            sendRootIntent = viewModel::handleIntent
                        )
                    }

                    navigation<Screen.BottomBarScreens>(startDestination = Screen.Characters) {
                        composable<Screen.Characters> { CharactersScreen(sendRootIntent = viewModel::handleIntent) }
                        composable<Screen.Favourites> { FavouritesScreen(sendRootIntent = viewModel::handleIntent) }
                        composable<Screen.Profile> { ProfileScreen(sendRootIntent = viewModel::handleIntent) }
                        composable<Screen.Settings> { SettingsScreen(sendRootIntent = viewModel::handleIntent) }
                    }
                }


//            state.release?.let {
//                UpdateAvailable(release = it) {
//                    viewModel.handleIntent(NavRootIntent.Update)
//                }
//            }
//            // Show loading indicator if update is in progress
//            if (state.isUpdateInProgress) UpdateProgress(percentage = state.percentage)
            }
        }
    }
}
