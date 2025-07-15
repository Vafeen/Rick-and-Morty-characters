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
import ru.vafeen.domain.local_database.repository.FavouritesLocalRepository
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersState
import ru.vafeen.presentation.ui.navigation.NavRootIntent

/**
 * ViewModel for the Characters screen handling character list, filters, and navigation.
 *
 * @property characterLocalRepository Repository for accessing local character data.
 * @property sendRootIntent Function for sending navigation intents to the root navigation handler.
 */
@HiltViewModel(assistedFactory = CharactersViewModel.Factory::class)
internal class CharactersViewModel @AssistedInject constructor(
    private val characterLocalRepository: CharacterLocalRepository,
    private val favouritesLocalRepository: FavouritesLocalRepository,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
) : ViewModel() {

    // Current filter state backing flow
    private val _currentFilters = MutableStateFlow(FiltersState())

    /**
     * Flow of paged character data filtered by current filters.
     * Combined with caching to optimize performance.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val charactersFlow = _currentFilters
        .flatMapLatest { filters ->
            filters.toPagingFlow()
        }
        .cachedIn(viewModelScope)

    private val _state = MutableStateFlow(CharactersState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<CharactersEffect>()

    /**
     * Shared flow for one-time side effects emitted by this ViewModel.
     */
    val effects = _effects.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            favouritesLocalRepository.getAllFavourites().collect { favourites ->
                _state.update { it.copy(favourites = favourites) }
            }
        }

    }

    /**
     * Handles user intents (actions) from the UI.
     *
     * @param intent The intent to process.
     */
    fun handleIntent(intent: CharactersIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is CharactersIntent.Refresh -> refresh()
                is CharactersIntent.ClickToCharacter -> clickToCharacter(intent.id)
                is CharactersIntent.ApplyFilters -> applyFilters(intent.filtersState)
                is CharactersIntent.ChangeFilterVisibility -> changeFilterVisibility(intent.isVisible)
                is CharactersIntent.ChangeIsFavourite -> changeIsFavourite(intent.id)
                is CharactersIntent.IsDataEmpty -> isDataEmpty(intent.isEmpty)
            }
        }
    }

    private fun isDataEmpty(isEmpty: Boolean) {
        _state.update { it.copy(dataIsEmpty = isEmpty) }
    }

    private suspend fun changeIsFavourite(id: Int) {
        val state = _state.value
        if (id in state.favourites) favouritesLocalRepository.removeFromFavourites(id)
        else favouritesLocalRepository.addToFavourites(id)
    }

    /**
     * Emits a refresh effect to notify UI to refresh character data.
     */
    private suspend fun refresh() {
        _effects.emit(CharactersEffect.Refresh)
    }

    /**
     * Applies the provided filters to character list and refreshes data.
     *
     * @param filtersState The new filters to apply.
     */
    private suspend fun applyFilters(filtersState: FiltersState) {
        _currentFilters.value = filtersState
        _state.update { it.copy(filtersState = filtersState) }
        changeFilterVisibility(false)
        refresh()
    }

    /**
     * Shows or hides the filters bottom sheet.
     *
     * @param isVisible True to show filters, false to hide.
     */
    private fun changeFilterVisibility(isVisible: Boolean) {
        _state.update { it.copy(isFilterBottomSheetVisible = isVisible) }
    }

    /**
     * Handles character click event by sending navigation intent.
     *
     * @param id The ID of the clicked character.
     */
    private fun clickToCharacter(id: Int) =
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.Character(id)))

    /**
     * Converts [FiltersState] into a PagingData flow from the repository.
     *
     * @param repo The local repository to fetch paged data from.
     * @return Flow of paged character data filtered by the filters.
     */
    private fun FiltersState.toPagingFlow() =
        characterLocalRepository.getPaged(
            name = name,
            status = status?.toString(),
            species = species,
            type = type,
            gender = gender?.toString()
        )

    /**
     * Assisted factory to create [CharactersViewModel] instances with parameterized constructor.
     */
    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: (NavRootIntent) -> Unit): CharactersViewModel
    }
}
