package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import ru.vafeen.domain.model.CharacterData

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CharacterItem(
    character: CharacterData,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    errorImage: Painter? = null,
    imageSize: Int = 128,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {

        // Основное изображение
        AsyncImage(
            model = remember(character.imageUrl) {
                ImageRequest.Builder(context)
                    .data(character.imageUrl)
                    .memoryCacheKey(character.imageUrl)
                    .diskCacheKey(character.imageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build()
            },
            contentDescription = character.name,
            modifier = Modifier.size(imageSize.dp),
            placeholder = placeholder,
            error = errorImage,
            onError = {
                // Логирование ошибки при необходимости
            }
        )

        Text(text = "id=${character.id}")
        Text(text = character.name, style = MaterialTheme.typography.titleMedium)
        Text(text = character.species, style = MaterialTheme.typography.bodyMedium)
    }
}