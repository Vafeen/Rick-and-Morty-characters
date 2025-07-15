package ru.vafeen.presentation.ui.screen.characters_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.common.components.CharacterItem
import ru.vafeen.presentation.ui.common.components.ErrorItem
import ru.vafeen.presentation.ui.common.components.LoadingItem

/**
 * Displays the Characters screen with a list of characters, supporting pull-to-refresh
 * and pagination. Handles loading, error, and refresh states.
 *
 * Uses the [CharactersViewModel] to observe character data and side effects.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharactersScreen() {
    val viewModel: CharactersViewModel = hiltViewModel()
    val characters = viewModel.charactersFlow.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullToRefreshState()

    // Collects side effects from the ViewModel, such as triggering a refresh.
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                CharactersEffect.Refresh -> {
                    characters.refresh()
                }
            }
        }
    }

    PullToRefreshBox(
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        isRefreshing = characters.loadState.refresh is LoadState.Loading,
        onRefresh = {
            viewModel.handleIntent(CharactersIntent.Refresh)
        }
    ) {
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
                                onClick = {
                                    viewModel.insert(entity.copy(name = "${entity.name}123"))
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
