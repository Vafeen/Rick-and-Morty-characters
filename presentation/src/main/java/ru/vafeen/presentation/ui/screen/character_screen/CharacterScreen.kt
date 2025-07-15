package ru.vafeen.presentation.ui.screen.character_screen

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.ui.navigation.NavRootIntent

@Composable
internal fun CharacterScreen(
    character: Screen.Character,
    sendRootIntent: (NavRootIntent) -> Unit
) {
    Text("${character.id}")
    BackHandler { sendRootIntent(NavRootIntent.Back) }
}