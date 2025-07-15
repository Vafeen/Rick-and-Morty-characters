package ru.vafeen.presentation.ui.screen.characters_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.model.CharacterData
import javax.inject.Inject

/**
 * ViewModel responsible for managing the Characters screen state.
 *
 * @property characterLocalRepository Repository for accessing character data.
 */
@HiltViewModel
internal class CharactersViewModel @Inject constructor(
    private val characterLocalRepository: CharacterLocalRepository,
) : ViewModel() {

    /**
     * Flow of paged character data, cached in the ViewModel scope.
     */
    val charactersFlow = characterLocalRepository.getPaged().cachedIn(viewModelScope)

    private val _effects = MutableSharedFlow<CharactersEffect>()

    /**
     * Shared flow of one-time side effects emitted by this ViewModel.
     */
    val effects = _effects.asSharedFlow()

    /**
     * Handles user intents (actions) from the UI.
     *
     * @param intent The intent to handle.
     */
    fun handleIntent(intent: CharactersIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is CharactersIntent.Refresh -> refresh()
            }
        }
    }

    /**
     * Emits a refresh effect to trigger data refresh in the UI.
     */
    private suspend fun refresh() {
        _effects.emit(CharactersEffect.Refresh)
    }

    /**
     * Inserts a new character into the local repository.
     *
     * @param characterData The character data to insert.
     */
    fun insert(characterData: CharacterData) {
        viewModelScope.launch(Dispatchers.IO) {
            characterLocalRepository.insert(listOf(characterData))
        }
    }
}
