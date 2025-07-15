package ru.vafeen.presentation.ui.screen.profile_screen

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.vafeen.presentation.ui.navigation.NavRootIntent

@Composable
internal fun ProfileScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    Text("profileScreen")
    BackHandler { sendRootIntent(NavRootIntent.Back) }
}