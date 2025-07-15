package ru.vafeen.presentation.ui.feature.settings_screen

import ru.vafeen.domain.model.Settings

internal sealed class SettingsIntent {
    data class SaveSettings(val saving: (Settings) -> Settings) : SettingsIntent()
    data object OpenColorEditDialog : SettingsIntent()
    data object CloseColorEditDialog : SettingsIntent()
    data class OpenLink(val link: String) : SettingsIntent()
    data class SendEmail(val email: String) : SettingsIntent()
}