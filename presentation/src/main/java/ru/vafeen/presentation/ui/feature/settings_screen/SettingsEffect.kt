package ru.vafeen.presentation.ui.feature.settings_screen

internal sealed class SettingsEffect {
    data class OpenLink(val link: String) : SettingsEffect()
    data class SendEmail(val email: String) : SettingsEffect()
}