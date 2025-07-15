package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.raedapps.alwan.rememberAlwanState
import com.raedapps.alwan.ui.Alwan
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.bottom_bar.BottomBarItem
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.ui.common.utils.suitableColor
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Dialog which allows the user to pick a color using a color picker UI.
 *
 * Displays a preview bar, color picker widget, and action buttons for cancelling,
 * applying the color, or resetting to the default theme color.
 *
 * @param firstColor The initial color shown when the dialog opens.
 * @param onDismissRequest Callback invoked when the dialog should be dismissed.
 * @param onChangeColorCallback Callback invoked with the newly selected color when user applies changes.
 */
@Composable
internal fun ColorPickerDialog(
    firstColor: Color,
    onDismissRequest: () -> Unit,
    onChangeColorCallback: (Color) -> Unit
) {
    val defaultColor = AppTheme.colors.mainColor
    val colorState = rememberAlwanState(initialColor = firstColor)
    var newColor by remember { mutableStateOf(firstColor) }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colors.background
            ),
            border = BorderStroke(width = 2.dp, color = AppTheme.colors.text)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.new_interface_color),
                    fontSize = FontSize.big22,
                    modifier = Modifier.padding(10.dp),
                    color = AppTheme.colors.text
                )

                BottomBar(
                    containerColor = newColor,
                    currentScreen = Screen.Favourites,
                    screens = listOf(
                        BottomBarItem(
                            screen = Screen.Characters,
                            icon = painterResource(R.drawable.characters),
                            contentDescription = stringResource(R.string.characters)
                        ),
                        BottomBarItem(
                            screen = Screen.Favourites,
                            icon = painterResource(R.drawable.favorite_full),
                            contentDescription = stringResource(R.string.favourites_screen)
                        ),
                        BottomBarItem(
                            screen = Screen.Profile,
                            icon = painterResource(R.drawable.profile),
                            contentDescription = stringResource(R.string.profile_screen)
                        )
                    ),
                    navigateTo = {}
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Color picker widget
                Alwan(
                    onColorChanged = { color -> newColor = color },
                    modifier = Modifier.size(300.dp),
                    state = colorState,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AppButton(
                        onClick = onDismissRequest,
                        color = newColor,
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = newColor.suitableColor()
                        )
                    }
                    if (firstColor != defaultColor) {
                        IconButton(onClick = {
                            onChangeColorCallback(defaultColor)
                            onDismissRequest()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_this_theme),
                                tint = AppTheme.colors.text
                            )
                        }
                    }
                    AppButton(
                        enabled = newColor.toArgb() != firstColor.toArgb(),
                        color = newColor,
                        onClick = {
                            onChangeColorCallback(newColor)
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.apply),
                            color = newColor.suitableColor()
                        )
                    }
                }
            }
        }
    }
}
