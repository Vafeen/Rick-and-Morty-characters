package ru.vafeen.presentation.ui.feature.settings_screen

/**
 * Represents one-time effects or events emitted from the Settings screen ViewModel,
 * such as navigation or external actions.
 */
internal sealed class SettingsEffect {

    /**
     * Effect to open a web link (URL) externally.
     *
     * @property link The URL to open.
     */
    data class OpenLink(val link: String) : SettingsEffect()

    /**
     * Effect to send an email using an external email client.
     *
     * @property email The email address to send to.
     */
    data class SendEmail(val email: String) : SettingsEffect()
}
