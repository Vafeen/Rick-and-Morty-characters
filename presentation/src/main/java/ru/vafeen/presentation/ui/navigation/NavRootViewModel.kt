package ru.vafeen.presentation.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.common.navigation.screenWithBottomBar
import javax.inject.Inject

/**
 *

 */
internal class NavRootViewModel @Inject constructor() : ViewModel() {

    private val _effects = MutableSharedFlow<NavRootEffect>()
    val effects = _effects.asSharedFlow()
    private val _state = MutableStateFlow(
        NavRootState(
            startScreen = Screen.BottomBarScreens,//Screen.SignIn(number = "", password = ""),
            isBottomBarVisible = true,
        )
    )
    val state = _state.asStateFlow()


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

    private fun updateCurrentScreen(screen: Screen) {
        _state.update {
            it.copy(
                currentScreen = screen,
                isBottomBarVisible = screen in screenWithBottomBar
            )
        }
    }

    private suspend fun navigateToScreen(screen: Screen) {
        _effects.emit(NavRootEffect.NavigateToScreen { navHostController ->
            navHostController.navigate(screen) {
                launchSingleTop = true
            }
        })
    }

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


    private suspend fun back() = _effects.emit(NavRootEffect.Back)
    private fun changeBottomBarVisibleOn(isVisible: Boolean) {
        _state.update { it.copy(isBottomBarVisible = isVisible) }
    }

}
