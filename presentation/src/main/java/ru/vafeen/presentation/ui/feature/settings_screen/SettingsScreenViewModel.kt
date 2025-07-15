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

@HiltViewModel
internal class SettingsScreenViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState(settingsManager.settingsFlow.value))
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect { settings ->
                _state.update { it.copy(settings = settings) }
            }
        }
    }

    private suspend fun openLink(link: String) {
        _effects.emit(SettingsEffect.OpenLink(link))
    }

    private suspend fun sendEmail(email: String) {
        _effects.emit(SettingsEffect.SendEmail(email))
    }

    private fun changeColorPickerDialogVisibility(isVisible: Boolean) {
        _state.update { it.copy(colorPickerDialogIsEditable = isVisible) }
    }

    private fun saveSettings(saving: (Settings) -> Settings) {
        settingsManager.save(saving = saving)
    }
}