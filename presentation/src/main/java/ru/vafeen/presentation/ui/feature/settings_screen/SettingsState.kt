package ru.vafeen.presentation.ui.feature.settings_screen

import ru.vafeen.domain.model.Settings

internal data class SettingsState(
    val settings: Settings,
    val colorPickerDialogIsEditable: Boolean = false
)