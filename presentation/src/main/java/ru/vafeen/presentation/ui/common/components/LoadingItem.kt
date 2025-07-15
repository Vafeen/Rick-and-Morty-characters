package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a small circular loading indicator.
 */
@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(20.dp)
            .padding(20.dp)
    )
}