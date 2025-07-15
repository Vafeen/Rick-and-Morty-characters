package ru.vafeen.presentation.ui.feature.characters_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersState
import ru.vafeen.presentation.ui.navigation.NavRootIntent

@HiltViewModel(assistedFactory = CharactersViewModel.Factory::class)
internal class CharactersViewModel @AssistedInject constructor(
    private val characterLocalRepository: CharacterLocalRepository,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
) : ViewModel() {

    private val _currentFilters = MutableStateFlow(FiltersState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val charactersFlow = _currentFilters.flatMapLatest {
        it.toPagingFlow(characterLocalRepository)
    }.cachedIn(viewModelScope)

    private val _state = MutableStateFlow(CharactersState())
    val state = _state.asStateFlow()

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
                is CharactersIntent.ApplyFilters -> applyFilters(intent.filtersState)
                is CharactersIntent.ChangeFilterVisibility -> changeFilterVisibility(intent.isVisible)
            }
        }
    }

    /**
     * Emits a refresh effect to trigger data refresh in the UI.
     */
    private suspend fun refresh() {
        _effects.emit(CharactersEffect.Refresh)
    }

    private suspend fun applyFilters(filtersState: FiltersState) {
        _currentFilters.value = filtersState
        _state.update { it.copy(filtersState = filtersState) }
        changeFilterVisibility(false)
        refresh()
    }

    private fun changeFilterVisibility(isVisible: Boolean) {
        _state.update { it.copy(isFilterBottomSheetVisible = isVisible) }
    }

    private fun clickToCharacter(id: Int) =
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.Character(id)))

    private fun FiltersState.toPagingFlow(repo: CharacterLocalRepository) =
        repo.getPaged(
            name = name,
            status = status?.toString(),
            species = species,
            type = type,
            gender = gender?.toString()
        )

    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: (NavRootIntent) -> Unit): CharactersViewModel
    }
}