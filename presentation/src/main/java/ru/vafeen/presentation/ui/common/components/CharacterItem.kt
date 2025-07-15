package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Gender
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * A composable component that displays a character card with detailed information.
 *
 * @param character The character data to display
 * @param modifier Modifier for styling the component
 * @param placeholder Painter to display while the image is loading
 * @param errorImage Painter to display if image loading fails
 * @param imageSize Size of the character image in dp
 * @param onClick Callback when the card is clicked
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun CharacterItem(
    character: CharacterData,
    isFavourite: Boolean,
    changeIsFavourite: () -> Unit,
    isChosen: Boolean,
    changeIsChosen: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    errorImage: Painter? = null,
    imageSize: Int = 120,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val cornerRadius = 12.dp

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = AppTheme.colors.buttonColor)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(12.dp)
        ) {
            // Top section with image and basic info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Character avatar
                AsyncImage(
                    model = remember(character.imageUrl) {
                        ImageRequest.Builder(context)
                            .data(character.imageUrl)
                            .crossfade(true)
                            .memoryCacheKey(character.imageUrl)
                            .diskCacheKey(character.imageUrl)
                            .build()
                    },
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(imageSize.dp)
                        .clip(RoundedCornerShape(cornerRadius)),
                    contentScale = ContentScale.Crop,
                    placeholder = placeholder,
                    error = errorImage,
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Main information column
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(modifier = Modifier.align(Alignment.End)) {
                        IconButton(
                            onClick = changeIsChosen
                        ) {
                            Icon(
                                painter = painterResource(if (isChosen) R.drawable.chosen_character else R.drawable.character),
                                contentDescription = stringResource(R.string.is_this_character_is_your)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            onClick = changeIsFavourite
                        ) {
                            Icon(
                                painter = painterResource(if (isFavourite) R.drawable.favorite_full else R.drawable.favourite),
                                contentDescription = stringResource(R.string.is_this_character_in_favourites)
                            )
                        }

                    }
                    // Species and type
                    CharacterDetailRow(
                        icon = Icons.Default.Pets,
                        text = buildString {
                            append(character.species)
                            character.type?.takeIf { it.isNotBlank() }?.let {
                                append(" ($it)")
                            }
                        }
                    )

                    // Gender information
                    CharacterDetailRow(
                        icon = when (character.gender) {
                            Gender.MALE -> Icons.Default.Male
                            Gender.FEMALE -> Icons.Default.Female
                            Gender.GENDERLESS -> Icons.Default.Transgender
                            else -> Icons.Default.QuestionMark
                        },
                        text = character.gender.toString().lowercase()
                            .replaceFirstChar { it.uppercase() }
                    )

                    // Life status indicator
                    LifeStatusIndicator(
                        status = character.lifeStatus,
                        modifier = Modifier
                            .wrapContentWidth()
                    )
                }
            }

            // Additional information section
            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                // Character name (supports multiline)
                ThisThemeText(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )

                // Origin location
                CharacterDetailRow(
                    icon = Icons.Default.Public,
                    text = "From: ${character.origin.name}"
                )

                // Current location
                CharacterDetailRow(
                    icon = Icons.Default.Place,
                    text = "Last seen: ${character.currentLocation.name}"
                )
            }
        }
    }
}
