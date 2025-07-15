package ru.vafeen.presentation.ui.feature.character_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.service.SettingsManager
import ru.vafeen.domain.use_case.FetchCharacterDataUseCase

/**
 * ViewModel for the Character screen that handles loading and exposing character data.
 *
 * @property settingsManager Manager for application settings.
 * @property fetchCharacterDataUseCase Use case to fetch character data.
 * @property id The unique character ID to load data for.
 */
@HiltViewModel(assistedFactory = CharacterViewModel.Factory::class)
internal class CharacterViewModel @AssistedInject constructor(
    private val settingsManager: SettingsManager,
    private val fetchCharacterDataUseCase: FetchCharacterDataUseCase,
    @Assisted private val id: Int,
) : ViewModel() {

    /**
     * [StateFlow] of application settings, exposed from [settingsManager].
     */
    val settingsFlow = settingsManager.settingsFlow

    /**
     * Backing [MutableStateFlow] holding the current UI state of the screen.
     */
    private val _state = MutableStateFlow(CharacterState(settings = settingsFlow.value))

    /**
     * Read-only [StateFlow] of the current UI state.
     */
    val state = _state.asStateFlow()

    init {
        // Automatically fetch character data on initialization.
        viewModelScope.launch(Dispatchers.IO) {
            fetchData()
        }
        // Observe changes in settings and update UI state accordingly.
        viewModelScope.launch(Dispatchers.IO) {
            settingsFlow.collect { settings ->
                _state.update {
                    it.copy(settings = settings)
                }
            }
        }
    }

    /**
     * Handles incoming intents (actions) from the UI layer.
     *
     * @param intent The [CharacterIntent] to handle.
     */
    fun handleIntent(intent: CharacterIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                CharacterIntent.FetchData -> fetchData()
            }
        }
    }

    /**
     * Fetches character data using the provided use case and updates the UI state.
     *
     * Loads the data asynchronously, sets loading and error states accordingly.
     * Also simulates a loading delay for demonstration.
     */
    private suspend fun fetchData() {
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
    }

    /**
     * Assisted factory interface to create instances of [CharacterViewModel]
     * with a required parameter [id].
     */
    @AssistedFactory
    interface Factory {
        /**
         * Creates an instance of [CharacterViewModel] with the specific character ID.
         *
         * @param id The unique character ID.
         * @return The created [CharacterViewModel] instance.
         */
        fun create(id: Int): CharacterViewModel
    }
}
