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

    BackHandler { sendRootIntent(NavRootIntent.Back) }
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

            if (state.colorPickerDialogIsEditable) {
                ColorPickerDialog(
                    firstColor = mainColor,
                    onDismissRequest = { viewModel.handleIntent(SettingsIntent.CloseColorEditDialog) },
                    onChangeColorCallback = {
                        viewModel.handleIntent(SettingsIntent.SaveSettings { s ->
                            if (dark) s.copy(darkThemeColor = it) else s.copy(lightThemeColor = it)
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


                Box(modifier = Modifier.fillMaxWidth()) {
                    // name of section
                    ThisThemeText(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.Center),
                        fontSize = FontSize.big22,
                        text = stringResource(R.string.interface_str)
                    )
                }
                // Color
                CardOfSettings(text = stringResource(R.string.interface_color), icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.palette),
                        contentDescription = "change color of interface",
                        tint = it.suitableColor()
                    )
                }, onClick = { viewModel.handleIntent(SettingsIntent.OpenColorEditDialog) })


                // name of section
                ThisThemeText(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = FontSize.big22,
                    text = stringResource(R.string.contacts)
                )

                // CODE
                CardOfSettings(text = stringResource(R.string.code), icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.terminal),
                        contentDescription = "view code",
                        tint = it.suitableColor()
                    )
                }, onClick = { viewModel.handleIntent(SettingsIntent.OpenLink(Link.CODE)) })

                CardOfSettings(text = stringResource(R.string.report_a_bug), icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.bug_report),
                        contentDescription = "view code",
                        tint = it.suitableColor()
                    )
                }, onClick = { viewModel.handleIntent(SettingsIntent.SendEmail(Link.MAIL)) })

                // version
                ThisThemeText(
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(bottom = 20.dp)
                        .align(Alignment.End),
                    fontSize = FontSize.small17,
                    text = "${stringResource(R.string.version)} ${LocalContext.current.getVersionName()}"
                )
            }
        }
    }
}