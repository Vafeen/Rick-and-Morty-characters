package ru.vafeen.presentation.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.service.SettingsManager
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.common.navigation.screenWithBottomBar
import javax.inject.Inject

/**
 * ViewModel responsible for handling navigation logic and state in the NavRoot.
 *
 * It processes navigation intents, updates UI state, and emits navigation effects
 * to be observed by the UI layer.
 */
@HiltViewModel
internal class NavRootViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val settingsFlow = settingsManager.settingsFlow
    private val _effects = MutableSharedFlow<NavRootEffect>()
    val effects = _effects.asSharedFlow()

    private val _state = MutableStateFlow(
        NavRootState(
            startScreen = Screen.BottomBarScreens, // Screen.SignIn(number = "", password = ""),
            isBottomBarVisible = true,
            settings = settingsFlow.value
        )
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsFlow.collect { settings ->
                _state.update { it.copy(settings = settings) }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect { settings ->
                _state.update { it.copy(isMyCharacterChosen = settings.yourCharacterId != null) }
            }
        }
    }

    /**
     * Handles navigation intents by dispatching to appropriate functions.
     *
     * @param intent The navigation intent to handle.
     */
    fun handleIntent(intent: NavRootIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is NavRootIntent.NavigateToScreen -> navigateToScreen(intent.screen)
                is NavRootIntent.NavigateToBottomBarScreen -> navigateToBottomBarScreen(intent.screen)
                NavRootIntent.Back -> back()
                is NavRootIntent.ChangeBottomBarVisible -> changeBottomBarVisibleOn(intent.isVisible)
                is NavRootIntent.UpdateCurrentScreen -> updateCurrentScreen(intent.screen)
                NavRootIntent.ClearBackStack -> _effects.emit(NavRootEffect.ClearBackStack)
            }
        }
    }

    /**
     * Updates the current screen and bottom bar visibility state.
     *
     * @param screen The screen to set as current.
     */
    private fun updateCurrentScreen(screen: Screen) {
        _state.update {
            it.copy(
                currentScreen = screen,
                isBottomBarVisible = screen in screenWithBottomBar
            )
        }
    }

    /**
     * Emits a navigation effect to navigate to the specified screen.
     *
     * @param screen The target screen to navigate to.
     */
    private suspend fun navigateToScreen(screen: Screen) {
        _effects.emit(NavRootEffect.NavigateToScreen { navHostController ->
            navHostController.navigate(screen) {
                launchSingleTop = true
            }
        })
    }

    /**
     * Emits a navigation effect to navigate to a bottom bar screen,
     * popping up to the start destination to clear intermediate destinations.
     *
     * @param screen The bottom bar screen to navigate to.
     */
    private suspend fun navigateToBottomBarScreen(screen: Screen) {
        _effects.emit(
            NavRootEffect.NavigateToScreen { navHostController ->
                navHostController.navigate(screen) {
                    popUpTo(navHostController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        )
    }

    /**
     * Emits a navigation effect to navigate back.
     */
    private suspend fun back() = _effects.emit(NavRootEffect.Back)

    /**
     * Updates the visibility state of the bottom navigation bar.
     *
     * @param isVisible True to show the bottom bar, false to hide.
     */
    private fun changeBottomBarVisibleOn(isVisible: Boolean) {
        _state.update { it.copy(isBottomBarVisible = isVisible) }
    }
}
