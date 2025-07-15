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
import ru.vafeen.domain.use_case.FetchCharacterDataUseCase

/**
 * ViewModel for the Character screen that handles loading and exposing character data.
 *
 * @property id The unique character ID to load data for.
 * @property fetchCharacterDataUseCase Use case to fetch character data.
 */
@HiltViewModel(assistedFactory = CharacterViewModel.Factory::class)
internal class CharacterViewModel @AssistedInject constructor(
    @Assisted private val id: Int,
    private val fetchCharacterDataUseCase: FetchCharacterDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterState())
    val state = _state.asStateFlow()

    init {
        // Automatically fetch character data on initialization.
        viewModelScope.launch(Dispatchers.IO) {
            fetchData()
        }
    }

    /**
     * Handles incoming intents from the UI.
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
     * Fetches character data via the use case and updates the UI state accordingly.
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
     * Assisted factory to create [CharacterViewModel] instances with parameterized constructor.
     */
    @AssistedFactory
    interface Factory {
        fun create(id: Int): CharacterViewModel
    }
}
