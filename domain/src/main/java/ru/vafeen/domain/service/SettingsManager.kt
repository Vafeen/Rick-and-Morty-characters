package ru.vafeen.domain.service

import kotlinx.coroutines.flow.StateFlow
import ru.vafeen.domain.model.Settings

/**
 * Interface for managing application settings in a reactive way.
 */
interface SettingsManager {

    /**
     * Reactive [StateFlow] providing current settings and updates when they change.
     */
    val settingsFlow: StateFlow<Settings>

    /**
     * Saves updated settings by applying a transformation function to the current settings.
     *
     * @param saving Function that takes existing [Settings] and returns updated [Settings].
     */
    fun save(saving: (Settings) -> Settings)
}
