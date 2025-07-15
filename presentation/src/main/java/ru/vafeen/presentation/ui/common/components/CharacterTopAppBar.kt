package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.common.utils.suitableColor

/**
 * Top app bar shown on the character detail screen.
 *
 * Displays the character's name as the title, with a back navigation icon.
 * The colors are adapted to the provided container background color to ensure proper contrast.
 *
 * @param containerColor Background color of the app bar container.
 * @param characterName Name of the character to display in the title.
 * @param onBackClick Callback invoked when the back navigation icon is clicked.
 * @param modifier Optional modifier for the top app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterTopAppBar(
    containerColor: Color,
    characterName: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    CenterAlignedTopAppBar(
        title = {
            SuitableColorText(
                text = characterName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                background = containerColor
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = containerColor.suitableColor(),
            navigationIconContentColor = containerColor.suitableColor()
        ),
        modifier = modifier.height(60.dp),
    )
}
