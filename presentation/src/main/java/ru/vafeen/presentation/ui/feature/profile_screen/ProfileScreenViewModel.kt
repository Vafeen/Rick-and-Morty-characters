package ru.vafeen.presentation.ui.feature.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.service.SettingsManager
import ru.vafeen.domain.use_case.FetchCharacterDataUseCase
import ru.vafeen.presentation.ui.feature.character_screen.CharacterIntent
import javax.inject.Inject


@HiltViewModel
internal class ProfileScreenViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    private val fetchCharacterDataUseCase: FetchCharacterDataUseCase
) : ViewModel() {
    private val settingsFlow = settingsManager.settingsFlow
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        // Automatically fetch character data on initialization.
        viewModelScope.launch(Dispatchers.IO) {
            val settings = settingsFlow.value
            _state.update { it.copy(id = settings.yourCharacterId) }
            fetchData()
        }

    }

    /**
     * Handles incoming intents from the UI.
     *
     * @param intent The [ru.vafeen.presentation.ui.feature.character_screen.CharacterIntent] to handle.
     */
    fun handleIntent(intent: CharacterIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                CharacterIntent.FetchData -> fetchData()
            }
        }
    }

    /**
     * Fetches character data via the use case and updates the UI state accordingly.
     */
    private suspend fun fetchData() {
        val id = settingsFlow.value.yourCharacterId
        if (id != null) {
            _state.update { it.copy(isLoading = true, isError = false) }

            val data = fetchCharacterDataUseCase.invoke(id)

            // Simulate loading delay for demonstration purposes.
            delay(1000)

            _state.update {
                it.copy(
                    isLoading = false,
                    isError = data == null,
                    characterData = data
                )
            }
        } else {
            _state.update { it.copy(isError = true) }
        }
    }

}
