package ru.vafeen.presentation.ui.feature.settings_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.model.Settings
import ru.vafeen.domain.service.SettingsManager
import javax.inject.Inject

/**
 * ViewModel responsible for managing the Settings screen UI state,
 * handling user intents, and emitting side effects.
 *
 * @property settingsManager Manager to access and modify app settings.
 */
@HiltViewModel
internal class SettingsScreenViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState(settingsManager.settingsFlow.value))
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        // Observe settings changes to update UI state accordingly
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect { settings ->
                _state.update { it.copy(settings = settings) }
            }
        }
    }

    /**
     * Handles user intents sent from the UI and performs corresponding actions.
     *
     * @param intent The intent representing a user action.
     */
    fun handleIntent(intent: SettingsIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is SettingsIntent.SaveSettings -> saveSettings(intent.saving)
                SettingsIntent.CloseColorEditDialog -> changeColorPickerDialogVisibility(false)
                SettingsIntent.OpenColorEditDialog -> changeColorPickerDialogVisibility(true)
                is SettingsIntent.OpenLink -> openLink(intent.link)
                is SettingsIntent.SendEmail -> sendEmail(intent.email)
            }
        }
    }

    /**
     * Emits a side effect to open a web link.
     *
     * @param link The URL to open.
     */
    private suspend fun openLink(link: String) {
        _effects.emit(SettingsEffect.OpenLink(link))
    }

    /**
     * Emits a side effect to send an email via external client.
     *
     * @param email The email address to use.
     */
    private suspend fun sendEmail(email: String) {
        _effects.emit(SettingsEffect.SendEmail(email))
    }

    /**
     * Updates UI state to show or hide the color picker dialog.
     *
     * @param isVisible True to show, false to hide the dialog.
     */
    private fun changeColorPickerDialogVisibility(isVisible: Boolean) {
        _state.update { it.copy(colorPickerDialogIsEditable = isVisible) }
    }

    /**
     * Saves the new settings by applying the provided transformation function.
     *
     * @param saving Function that takes current settings and returns updated settings.
     */
    private fun saveSettings(saving: (Settings) -> Settings) {
        settingsManager.save(saving = saving)
    }
}
