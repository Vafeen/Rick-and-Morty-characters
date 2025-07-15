package ru.vafeen.domain.service

import kotlinx.coroutines.flow.StateFlow
import ru.vafeen.domain.model.Settings

interface SettingsManager {
    val settingsFlow: StateFlow<Settings>
    fun save(saving: (Settings) -> Settings)
}