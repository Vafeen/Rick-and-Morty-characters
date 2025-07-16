package ru.vafeen.presentation.ui.feature.characters_screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
import ru.vafeen.presentation.ui.common.utils.getMainColorForThisTheme
import ru.vafeen.presentation.ui.common.utils.suitableColor
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersBottomSheet
import ru.vafeen.presentation.ui.navigation.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Displays the Characters screen with a list of characters, supporting pull-to-refresh
 * and pagination. Handles loading, error, and refresh states.
 *
 * Uses the [CharactersViewModel] to observe character data and side effects.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharactersScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel: CharactersViewModel =
        hiltViewModel<CharactersViewModel, CharactersViewModel.Factory>(
            creationCallback = { it.create(sendRootIntent) }
        )
    val characters = viewModel.charactersFlow.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsState()
    val mainColor by rememberUpdatedState(
        state.settings.getMainColorForThisTheme(isSystemInDarkTheme())
            ?: AppTheme.colors.mainColor,
    )
    LaunchedEffect(characters.itemCount) {
        viewModel.handleIntent(CharactersIntent.IsDataEmpty(characters.itemCount == 0))
    }
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                CharactersEffect.Refresh -> {
                    characters.refresh()
                }
            }
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.background,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handleIntent(CharactersIntent.ChangeFilterVisibility(true)) },
                containerColor = mainColor,
                contentColor = mainColor.suitableColor()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.filters),
                    contentDescription = "Filters"
                )
            }
        }
    ) { innerPadding ->
        val padding = innerPadding
        if (state.isFilterBottomSheetVisible) {
            FiltersBottomSheet(
                initialFilters = state.filters,
                onFiltersApplied = { filters ->
                    viewModel.handleIntent(CharactersIntent.ApplyFilters(filters))
                },
                onDismissRequest = {
                    viewModel.handleIntent(CharactersIntent.ChangeFilterVisibility(false))
                }
            )
        }

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = if (state.dataIsEmpty) Alignment.Center else Alignment.TopCenter,
            isRefreshing = characters.loadState.refresh is LoadState.Loading,
            onRefresh = { viewModel.handleIntent(CharactersIntent.Refresh) }
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(count = characters.itemCount) { index ->
                            val entity = characters[index]
                            if (entity != null) {
                                CharacterItem(
                                    character = entity,
                                    placeholder = painterResource(R.drawable.placeholder),
                                    isFavourite = entity.id in state.favourites,
                                    changeIsFavourite = {
                                        viewModel.handleIntent(
                                            CharactersIntent.ChangeIsFavourite(
                                                entity.id
                                            )
                                        )
                                    },
                                    onClick = {
                                        viewModel.handleIntent(
                                            CharactersIntent.ClickToCharacter(
                                                entity.id
                                            )
                                        )
                                    },
                                    isChosen = state.settings.yourCharacterId == entity.id,
                                    changeIsChosen = {
                                        viewModel.handleIntent(
                                            CharactersIntent.SetIsMyCharacter(entity.id)
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
