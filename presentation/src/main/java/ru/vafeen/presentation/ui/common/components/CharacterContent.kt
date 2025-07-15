package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Gender
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Composable which displays detailed content for a character.
 *
 * Shows character image, status, details like species, gender,
 * origin and current location, and related episodes.
 *
 * @param character The [CharacterData] to display.
 * @param modifier Optional [Modifier] for styling.
 */
@Composable
internal fun CharacterContent(
    character: CharacterData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Character Image with status badge overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Status badge overlay at top end
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                LifeStatusIndicator(status = character.lifeStatus)
            }
        }

        // Details section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Character name
            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.text,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Species and optional type displayed in one line
            CharacterDetailRow(
                icon = Icons.Default.Pets,
                text = buildString {
                    append(character.species)
                    character.type?.takeIf { it.isNotBlank() }?.let {
                        append(" ($it)")
                    }
                }
            )

            // Gender with corresponding icon
            CharacterDetailRow(
                icon = when (character.gender) {
                    Gender.MALE -> Icons.Default.Male
                    Gender.FEMALE -> Icons.Default.Female
                    Gender.GENDERLESS -> Icons.Default.Transgender
                    else -> Icons.Default.Transgender
                },
                text = character.gender.toString().lowercase()
                    .replaceFirstChar { it.uppercase() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Origin location card
            InfoCard(
                title = "Origin",
                icon = Icons.Default.Public,
                content = character.origin.name
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Last known location card
            InfoCard(
                title = "Last Known Location",
                icon = Icons.Default.Place,
                content = character.currentLocation.name
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Episodes header
            Text(
                text = "Episodes (${character.episodeIds.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.text,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // List of episodes
            character.episodeIds.forEach { episodeId ->
                Text(
                    text = "Episode $episodeId",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.text,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Character creation date
            ThisThemeText(
                text = "Created: ${character.createdAt.toLocalDate()}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End)
            )
        }
    }
}
