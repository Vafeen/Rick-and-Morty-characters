package ru.vafeen.presentation.ui.feature.characters_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.ui.navigation.NavRootIntent

/**
 * ViewModel responsible for managing the Characters screen state.
 *
 * @property characterLocalRepository Repository for accessing character data.
 */
@HiltViewModel(assistedFactory = CharactersViewModel.Factory::class)
internal class CharactersViewModel @AssistedInject constructor(
    private val characterLocalRepository: CharacterLocalRepository,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
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
                is CharactersIntent.ClickToCharacter -> clickToCharacter(intent.id)
            }
        }
    }

    /**
     * Emits a refresh effect to trigger data refresh in the UI.
     */
    private suspend fun refresh() {
        _effects.emit(CharactersEffect.Refresh)
    }


    private fun clickToCharacter(id: Int) =
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.Character(id)))


    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: (NavRootIntent) -> Unit): CharactersViewModel
    }
}
