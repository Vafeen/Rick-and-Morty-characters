package ru.vafeen.presentation.ui.common.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.model.LifeStatus


/**
 * A visual indicator for a character's life status.
 *
 * @param modifier Modifier for styling the component
 * @param status The life status to display (Alive, Dead, or Unknown)
 */
@Composable
internal fun LifeStatusIndicator(
    modifier: Modifier = Modifier,
    status: LifeStatus
) {
    val (color, text) = when (status) {
        LifeStatus.ALIVE -> Color.Green to "Alive"
        LifeStatus.DEAD -> Color.Red to "Dead"
        else -> Color.Gray to "Unknown"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        ThisThemeText(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}