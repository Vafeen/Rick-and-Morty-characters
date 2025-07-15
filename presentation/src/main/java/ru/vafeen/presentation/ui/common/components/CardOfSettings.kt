package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.common.utils.generateRandomColor
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * A reusable card component for displaying settings options,
 * with an icon, text, and optional additional content revealed on click.
 *
 * @param text The main text displayed on the card.
 * @param icon A composable lambda that draws an icon, given a [Color] applied to the icon background.
 * @param onClick Callback invoked when the card is clicked.
 * @param additionalContentIsVisible Optional flag to conditionally show [additionalContent].
 * @param additionalContent Optional composable content displayed below the main row when visible.
 */
@Composable
internal fun CardOfSettings(
    text: String,
    icon: @Composable (Color) -> Unit,
    onClick: () -> Unit,
    additionalContentIsVisible: Boolean? = null,
    additionalContent: @Composable ((padding: Dp) -> Unit)? = null
) {
    val color = generateRandomColor()
    val contentPadding = 10.dp
    Card(
        modifier = Modifier.padding(vertical = 15.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.buttonColor,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = contentPadding)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .background(color, CircleShape)
                            .padding(3.dp)
                    ) {
                        icon(color)
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    ThisThemeText(
                        modifier = Modifier.padding(10.dp),
                        fontSize = FontSize.small17,
                        text = text,
                        maxLines = 2
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = "open section",
                    tint = AppTheme.colors.text
                )
            }
            if (additionalContentIsVisible == true)
                additionalContent?.let { it(contentPadding) }
        }
    }
}
