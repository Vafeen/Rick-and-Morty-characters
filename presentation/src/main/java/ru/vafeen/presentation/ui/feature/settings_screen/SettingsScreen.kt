package ru.vafeen.presentation.ui.feature.settings_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.utils.openLink
import ru.vafeen.presentation.common.utils.sendEmail
import ru.vafeen.presentation.ui.common.components.CardOfSettings
import ru.vafeen.presentation.ui.common.components.ColorPickerDialog
import ru.vafeen.presentation.ui.common.components.ThisThemeText
import ru.vafeen.presentation.ui.common.utils.Link
import ru.vafeen.presentation.ui.common.utils.getMainColorForThisTheme
import ru.vafeen.presentation.ui.common.utils.getVersionName
import ru.vafeen.presentation.ui.common.utils.suitableColor
import ru.vafeen.presentation.ui.navigation.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Composable screen displaying the app settings, including interface customization,
 * contact information, and external links.
 *
 * Supports editing theme colors via a color picker dialog.
 *
 * @param sendRootIntent Function to send navigation intents to the root view model.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel: SettingsScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val dark = isSystemInDarkTheme()
    val mainDefaultColor = AppTheme.colors.mainColor
    val mainColor by remember {
        derivedStateOf {
            state.settings.getMainColorForThisTheme(isDark = dark) ?: mainDefaultColor
        }
    }

    // Handle back navigation with system back button or UI
    BackHandler { sendRootIntent(NavRootIntent.Back) }

    // Collect one-time effects such as opening links or sending emails
    LaunchedEffect(null) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SettingsEffect.OpenLink -> context.openLink(effect.link)
                is SettingsEffect.SendEmail -> context.sendEmail(effect.email)
            }
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.background,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {

            // Show color picker dialog if enabled in the state
            if (state.colorPickerDialogIsEditable) {
                ColorPickerDialog(
                    firstColor = mainColor,
                    onDismissRequest = { viewModel.handleIntent(SettingsIntent.CloseColorEditDialog) },
                    onChangeColorCallback = { selectedColor ->
                        viewModel.handleIntent(SettingsIntent.SaveSettings { s ->
                            if (dark) s.copy(darkThemeColor = selectedColor)
                            else s.copy(lightThemeColor = selectedColor)
                        })
                    })
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
            ) {

                // Interface section header
                Box(modifier = Modifier.fillMaxWidth()) {
                    ThisThemeText(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.Center),
                        fontSize = FontSize.big22,
                        text = stringResource(R.string.interface_str)
                    )
                }

                // Interface color setting card
                CardOfSettings(
                    text = stringResource(R.string.interface_color),
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.palette),
                            contentDescription = stringResource(R.string.interface_color),
                            tint = it.suitableColor()
                        )
                    },
                    onClick = { viewModel.handleIntent(SettingsIntent.OpenColorEditDialog) }
                )

                // Contacts section header
                ThisThemeText(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = FontSize.big22,
                    text = stringResource(R.string.contacts)
                )

                // "Code" external link card
                CardOfSettings(
                    text = stringResource(R.string.code),
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.terminal),
                            contentDescription = stringResource(R.string.code),
                            tint = it.suitableColor()
                        )
                    },
                    onClick = { viewModel.handleIntent(SettingsIntent.OpenLink(Link.CODE)) }
                )

                // "Report a bug" email card
                CardOfSettings(
                    text = stringResource(R.string.report_a_bug),
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.bug_report),
                            contentDescription = stringResource(R.string.report_a_bug),
                            tint = it.suitableColor()
                        )
                    },
                    onClick = { viewModel.handleIntent(SettingsIntent.SendEmail(Link.MAIL)) }
                )

                // App version text aligned to the end
                ThisThemeText(
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(bottom = 20.dp)
                        .align(Alignment.End),
                    fontSize = FontSize.small17,
                    text = "${stringResource(R.string.version)} ${context.getVersionName()}"
                )
            }
        }
    }
}
