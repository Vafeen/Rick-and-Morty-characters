package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Displays an error message with a retry button.
 *
 * @param message The error message to display.
 * @param modifier Modifier to apply to this composable.
 * @param onClickRetry Callback invoked when the retry button is clicked.
 */
@Composable
internal fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = AppTheme.colors.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onClickRetry) {
            Text(text = "Retry")
        }
    }
}