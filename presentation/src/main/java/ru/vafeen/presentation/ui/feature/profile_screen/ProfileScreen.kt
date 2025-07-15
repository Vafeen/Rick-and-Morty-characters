package ru.vafeen.presentation.ui.feature.profile_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.ui.common.components.CharacterContent
import ru.vafeen.presentation.ui.common.components.CharacterTopAppBar
import ru.vafeen.presentation.ui.common.components.ErrorItem
import ru.vafeen.presentation.ui.common.utils.getMainColorForThisTheme
import ru.vafeen.presentation.ui.feature.character_screen.CharacterIntent
import ru.vafeen.presentation.ui.navigation.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Composable screen to display details of a character.
 *
 * @param sendRootIntent Function to send navigation intents to root view model.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel = hiltViewModel<ProfileScreenViewModel>()
    val state by viewModel.state.collectAsState()
    val mainColor by rememberUpdatedState(
        state.settings.getMainColorForThisTheme(
            isSystemInDarkTheme()
        ) ?: AppTheme.colors.mainColor
    )
    // Handle back press to navigate back via root intent
    BackHandler { sendRootIntent(NavRootIntent.Back) }
    Scaffold(
        topBar = {
            if (!state.isLoading && !state.isError && state.characterData != null) {
                state.characterData?.let {
                    CharacterTopAppBar(
                        characterName = it.name,
                        onBackClick = { sendRootIntent(NavRootIntent.Back) },
                        containerColor = mainColor
                    )
                }
            }
        },
        containerColor = AppTheme.colors.background
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            contentAlignment = Alignment.Center,
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.handleIntent(CharacterIntent.FetchData) }
        ) {
            // Show error UI if loading failed
            state.let { state ->
                if (state.isError) {
                    ErrorItem(
                        message = "error",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        viewModel.handleIntent(CharacterIntent.FetchData)
                    }
                }
                // Show character content when loaded without errors
                if (!state.isLoading && !state.isError && state.characterData != null) {
                    CharacterContent(character = state.characterData)
                }
            }

        }

    }


}
