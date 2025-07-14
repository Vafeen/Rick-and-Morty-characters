package ru.vafeen.presentation.ui.screen.main_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.vafeen.domain.model.CharacterData

@Composable
internal fun CharactersScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val characters = viewModel.charactersFlow.collectAsLazyPagingItems()
    Box(modifier = Modifier.fillMaxSize()) {
        when (characters.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

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

                    items(count = characters.itemCount) {
                        val entity =
                            characters[it]
                        if (entity != null) {
                            CharacterItem(entity) {
                                viewModel.insert(entity.copy(name = "${entity.name}123"))
                            }
                        } else {
                            LoadingItem(entity)
                        }
                    }

                    // Обработка ошибок при подгрузке
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
                    // Добавляем индикатор загрузки при подгрузке
                    if (characters.loadState.append is LoadState.Loading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: CharacterData, onClick: () -> Unit) {
    // Реализация отображения одного персонажа
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(character.imageUrl)
//                .crossfade(true)
//                .build(),
//            contentDescription = character.name,
//            modifier = Modifier.size(128.dp)
//        )
        Text(text = "id=${character.id}")
        Text(text = character.name, style = MaterialTheme.typography.titleMedium)
        Text(text = character.species, style = MaterialTheme.typography.bodyMedium)
        // Другие поля персонажа
    }
}


@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onClickRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun LoadingItem(characterData: CharacterData?) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(20.dp)
            .padding(20.dp)
    )
}