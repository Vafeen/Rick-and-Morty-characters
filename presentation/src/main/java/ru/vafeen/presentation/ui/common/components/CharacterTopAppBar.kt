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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.common.utils.suitableColor
import ru.vafeen.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterTopAppBar(
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
                background = AppTheme.colors.mainColor
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
            containerColor = AppTheme.colors.mainColor,
            titleContentColor = AppTheme.colors.mainColor.suitableColor(),
            navigationIconContentColor = AppTheme.colors.mainColor.suitableColor()
        ),
        modifier = modifier.height(60.dp),
    )
}
