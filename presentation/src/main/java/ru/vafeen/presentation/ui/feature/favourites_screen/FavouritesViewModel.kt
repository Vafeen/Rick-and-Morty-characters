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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.repository.CharacterLocalRepository
import ru.vafeen.domain.local_database.repository.FavouritesLocalRepository
import ru.vafeen.presentation.common.navigation.Screen
import ru.vafeen.presentation.ui.feature.favourites_screen.FavouritesIntent
import ru.vafeen.presentation.ui.navigation.NavRootIntent

/**
 * ViewModel for the Characters screen handling character list, filters, and navigation.
 *
 * @property characterLocalRepository Repository for accessing local character data.
 * @property sendRootIntent Function for sending navigation intents to the root navigation handler.
 */
@HiltViewModel(assistedFactory = FavouritesViewModel.Factory::class)
internal class FavouritesViewModel @AssistedInject constructor(
    private val characterLocalRepository: CharacterLocalRepository,
    private val favouritesLocalRepository: FavouritesLocalRepository,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
) : ViewModel() {
    /**
     * Flow of paged character data filtered by current filters.
     * Combined with caching to optimize performance.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val favouriteCharactersFlow = favouritesLocalRepository.getAllFavourites()
        .flatMapLatest { favourites ->
            favourites.toPagingFlow()
        }
        .cachedIn(viewModelScope)


    private val _effects = MutableSharedFlow<CharactersEffect>()

    /**
     * Shared flow for one-time side effects emitted by this ViewModel.
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
            }
        }
    }

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
