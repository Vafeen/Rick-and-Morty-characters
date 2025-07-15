package ru.vafeen.presentation.ui.feature.favourites_screen

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
import ru.vafeen.presentation.ui.feature.characters_screen.CharactersEffect
import ru.vafeen.presentation.ui.navigation.NavRootIntent

/**
 * ViewModel for the Favourites screen handling favourite characters list
 * and related user actions including refresh and navigation.
 *
 * @property characterLocalRepository Repository for accessing local character data.
 * @property favouritesLocalRepository Repository for managing favourite characters.
 * @property sendRootIntent Function for sending navigation intents to the root navigation handler.
 */
@HiltViewModel(assistedFactory = FavouritesViewModel.Factory::class)
internal class FavouritesViewModel @AssistedInject constructor(
    private val characterLocalRepository: CharacterLocalRepository,
    private val favouritesLocalRepository: FavouritesLocalRepository,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
) : ViewModel() {

    /**
     * Flow of paged favourite character data.
     * It observes the list of favourite IDs and maps them to a paged flow of [CharacterEntity].
     * Result is cached within [viewModelScope].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val favouriteCharactersFlow = favouritesLocalRepository.getAllFavourites()
        .flatMapLatest { favourites ->
            favourites.toPagingFlow()
        }
        .cachedIn(viewModelScope)
    private val _state = MutableStateFlow(FavouritesState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<CharactersEffect>()

    /**
     * Shared flow for one-time side effects emitted by this ViewModel,
     * e.g., refresh requests triggered by user actions.
     */
    val effects = _effects.asSharedFlow()

    /**
     * Handles user intents (actions) from the UI.
     *
     * @param intent The intent to process.
     */
    fun handleIntent(intent: FavouritesIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is FavouritesIntent.Refresh -> refresh()
                is FavouritesIntent.ClickToCharacter -> clickToCharacter(intent.id)
                is FavouritesIntent.ChangeIsFavourite -> changeIsFavourite(intent.id)
                is FavouritesIntent.IsDataEmpty -> dataIsEmpty(intent.isEmpty)
            }
        }
    }

    private fun dataIsEmpty(isEmpty: Boolean) {
        _state.update { it.copy(dataIsEmpty = isEmpty) }
    }

    /**
     * Removes a character from the favourites list by its ID.
     *
     * @param id The ID of the character to remove from favourites.
     */
    private suspend fun changeIsFavourite(id: Int) =
        favouritesLocalRepository.removeFromFavourites(id)

    /**
     * Emits a refresh effect to notify UI to refresh character data.
     */
    private suspend fun refresh() {
        _effects.emit(CharactersEffect.Refresh)
    }

    /**
     * Handles character click event by sending navigation intent.
     *
     * @param id The ID of the clicked character.
     */
    private fun clickToCharacter(id: Int) =
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.Character(id)))

    /**
     * Extension function to convert a list of favourite character IDs into a paging flow
     * by querying the local character repository.
     *
     * @receiver List of favourite character IDs.
     * @return Paging data flow of favourite characters.
     */
    private fun List<Int>.toPagingFlow() =
        characterLocalRepository.getFavourites(favourites = this)

    /**
     * Assisted factory to create [FavouritesViewModel] instances with parameterized constructor.
     */
    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: (NavRootIntent) -> Unit): FavouritesViewModel
    }
}
