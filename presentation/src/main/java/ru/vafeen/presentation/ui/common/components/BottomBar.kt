package ru.vafeen.presentation.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.vafeen.presentation.common.bottom_bar.BottomBarItem
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.ui.common.utils.suitableColor

/**
 * Bottom navigation bar component for a Jetpack Compose application.
 *
 * Displays a navigation bar with items provided in the [screens] list.
 * Highlights the currently selected screen [currentScreen].
 * Invokes [navigateTo] callback when a navigation item is clicked to navigate to the corresponding screen.
 *
 * @param containerColor The background color of the navigation bar.
 * @param currentScreen The currently active screen that is highlighted on the navigation bar.
 * @param screens A list of navigation bar items, each containing a screen, icon, and description.
 * @param navigateTo Callback function to navigate to the selected screen.
 */
@Composable
internal fun BottomBar(
    containerColor: Color,
    currentScreen: Screen,
    screens: List<BottomBarItem>,
    navigateTo: (Screen) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(60.dp),
        containerColor = containerColor
    ) {
        val size = screens.size
        screens.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.weight(1f / size),
                selected = currentScreen == item.screen,
                enabled = currentScreen != item.screen,
                onClick = { navigateTo(item.screen) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    disabledIconColor = containerColor.suitableColor(),
                    unselectedIconColor = containerColor.suitableColor().copy(alpha = 0.5f)
                ),
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.contentDescription
                    )
                }
            )
        }
    }
}
