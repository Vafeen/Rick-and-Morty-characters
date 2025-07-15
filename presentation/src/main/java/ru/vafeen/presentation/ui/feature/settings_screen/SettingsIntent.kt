package ru.vafeen.presentation.ui.feature.settings_screen

import ru.vafeen.domain.model.Settings

/**
 * Represents user intents (actions) for the Settings screen.
 */
internal sealed class SettingsIntent {

    /**
     * Intent to save settings by applying the given transformation function.
     *
     * @param saving A function which takes current [Settings] and returns the updated [Settings].
     */
    data class SaveSettings(val saving: (Settings) -> Settings) : SettingsIntent()

    /**
     * Intent to open the color edit dialog.
     */
    data object OpenColorEditDialog : SettingsIntent()

    /**
     * Intent to close the color edit dialog.
     */
    data object CloseColorEditDialog : SettingsIntent()

    /**
     * Intent to open a web link (URL).
     *
     * @param link The URL to open.
     */
    data class OpenLink(val link: String) : SettingsIntent()

    /**
     * Intent to send an email.
     *
     * @param email The email address to send to.
     */
    data class SendEmail(val email: String) : SettingsIntent()
}
