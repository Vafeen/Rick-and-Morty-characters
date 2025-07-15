package ru.vafeen.presentation.ui.feature.settings_screen

import ru.vafeen.domain.model.Settings

/**
 * Represents the UI state for the Settings screen.
 *
 * @property settings The current application settings.
 * @property colorPickerDialogIsEditable Indicates whether the color picker dialog is visible.
 */
internal data class SettingsState(
    val settings: Settings,
    val colorPickerDialogIsEditable: Boolean = false
)
