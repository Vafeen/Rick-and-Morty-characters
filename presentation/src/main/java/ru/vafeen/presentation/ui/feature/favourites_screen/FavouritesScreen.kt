package ru.vafeen.presentation.ui.feature.favourites_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.common.components.CharacterItem
import ru.vafeen.presentation.ui.common.components.ErrorItem
import ru.vafeen.presentation.ui.common.components.LoadingItem
import ru.vafeen.presentation.ui.common.components.ThisThemeText
import ru.vafeen.presentation.ui.feature.characters_screen.CharactersEffect
import ru.vafeen.presentation.ui.navigation.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Displays the Favourites screen showing a list of favourite characters.
 *
 * Supports paging and pull-to-refresh functionality.
 *
 * Handles various UI states like loading, error, and data display.
 *
 * Observes [FavouritesViewModel] for state and side effects.
 *
 * @param sendRootIntent Function to communicate navigation intents to the root navigator.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavouritesScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel: FavouritesViewModel =
        hiltViewModel<FavouritesViewModel, FavouritesViewModel.Factory>(
            creationCallback = { it.create(sendRootIntent) }
        )
    val characters = viewModel.favouriteCharactersFlow.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsState()
    // Collects side effects such as refresh requests, triggered externally or internally
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                CharactersEffect.Refresh -> {
                    characters.refresh()
                }
            }
        }
    }
    LaunchedEffect(characters.itemCount) {
        viewModel.handleIntent(FavouritesIntent.IsDataEmpty(characters.itemCount == 0))
    }
    Scaffold(
        containerColor = AppTheme.colors.background,
    ) { innerPadding ->
        val x = innerPadding

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            isRefreshing = characters.loadState.refresh is LoadState.Loading,
            onRefresh = { viewModel.handleIntent(FavouritesIntent.Refresh) }
        ) {
            if (state.dataIsEmpty) {
                ThisThemeText(stringResource(R.string.data_is_empty))
            }
            when (characters.loadState.refresh) {
                is LoadState.Error -> {
                    val error = characters.loadState.refresh as LoadState.Error
                    ErrorItem(
                        message = error.error.localizedMessage ?: "Unknown error",
                        modifier = Modifier.align(Alignment.Center),
                        onClickRetry = { characters.retry() }
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2)
                    ) {
                        items(count = characters.itemCount) { index ->
                            val entity = characters[index]
                            if (entity != null) {
                                CharacterItem(
                                    character = entity,
                                    placeholder = painterResource(R.drawable.placeholder),
                                    isFavourite = true,
                                    changeIsFavourite = {
                                        viewModel.handleIntent(
                                            FavouritesIntent.ChangeIsFavourite(
                                                entity.id
                                            )
                                        )
                                    },
                                    onClick = {
                                        viewModel.handleIntent(
                                            FavouritesIntent.ClickToCharacter(
                                                entity.id
                                            )
                                        )
                                    },
                                    isChosen = state.settings.yourCharacterId == entity.id,
                                    changeIsChosen = {
                                        viewModel.handleIntent(
                                            FavouritesIntent.SetIsMyCharacter(entity.id)
                                        )
                                    }
                                )
                            } else {
                                LoadingItem()
                            }
                        }

                        if (characters.loadState.append is LoadState.Error) {
                            val error = characters.loadState.append as LoadState.Error
                            item {
                                ErrorItem(
                                    message = error.error.localizedMessage ?: "Load more error",
                                    modifier = Modifier.fillMaxWidth(),
                                    onClickRetry = { characters.retry() }
                                )
                            }
                        }

                        if (characters.loadState.append is LoadState.Loading) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.Red,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
