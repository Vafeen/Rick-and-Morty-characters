package ru.vafeen.presentation.ui.feature.filters_bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.domain.service.SettingsManager

/**
 * ViewModel for managing filter UI state and user interactions in the filters bottom sheet.
 *
 * @property initialState The initial filters state when the ViewModel is created.
 */
@HiltViewModel(assistedFactory = FiltersViewModel.Factory::class)
class FiltersViewModel @AssistedInject constructor(
    private val settingsManager: SettingsManager,
    @Assisted initialState: Filters
) : ViewModel() {
    private val settingsFlow = settingsManager.settingsFlow
    private val _state = MutableStateFlow(
        FilterState(
            filters = initialState,
            settings = settingsFlow.value
        )
    )
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<FiltersEffect>()
    val effects = _effects.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsFlow.collect { settings ->
                _state.update { it.copy(settings = settings) }
            }
        }
    }

    /**
     * Handles incoming intents (user actions) related to filters.
     *
     * @param event The filter intent to process.
     */
    fun handleEvent(event: FiltersIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is FiltersIntent.NameChanged -> updateName(event.name)
                is FiltersIntent.StatusChanged -> updateStatus(event.status)
                is FiltersIntent.SpeciesChanged -> updateSpecies(event.species)
                is FiltersIntent.TypeChanged -> updateType(event.type)
                is FiltersIntent.GenderChanged -> updateGender(event.gender)
                FiltersIntent.ApplyFilters -> applyFilters()
                FiltersIntent.ResetFilters -> resetFilters()
            }
        }
    }

    /**
     * Updates the filter state with a new name.
     *
     * Converts empty string to null to represent no filter.
     *
     * @param name The new name filter value.
     */
    private fun updateName(name: String) {
        _state.update { it.copy(filters = it.filters.copy(name = name.ifEmpty { null })) }
    }

    /**
     * Updates the filter state with a new life status.
     *
     * @param status The new life status filter value, or null to clear it.
     */
    private fun updateStatus(status: LifeStatus?) {
        _state.update { it.copy(filters = it.filters.copy(status = status)) }
    }

    /**
     * Updates the filter state with a new species.
     *
     * Converts empty string to null to represent no filter.
     *
     * @param species The new species filter value.
     */
    private fun updateSpecies(species: String) {
        _state.update { it.copy(filters = it.filters.copy(species = species.ifEmpty { null })) }
    }

    /**
     * Updates the filter state with a new type.
     *
     * Converts empty string to null to represent no filter.
     *
     * @param type The new type filter value.
     */
    private fun updateType(type: String) {
        _state.update { it.copy(filters = it.filters.copy(type = type.ifEmpty { null })) }
    }

    /**
     * Updates the filter state with a new gender.
     *
     * @param gender The new gender filter value, or null to clear it.
     */
    private fun updateGender(gender: Gender?) {
        _state.update { it.copy(filters = it.filters.copy(gender = gender)) }
    }

    /**
     * Emits a one-time effect indicating that filters have been applied.
     *
     * This effect is typically observed by the UI to react accordingly (e.g., dismiss sheet).
     */
    private suspend fun applyFilters() {
        _effects.emit(FiltersEffect.FiltersApplied(_state.value.filters))
    }

    /**
     * Resets all filter parameters to their default (empty) state and applies the reset filters.
     */
    private suspend fun resetFilters() {
        _state.update { it.copy(filters = Filters()) }
        applyFilters()
    }

    /**
     * Assisted factory for creating instances of [FiltersViewModel] with custom initial state.
     */
    @AssistedFactory
    interface Factory {
        fun create(initialState: Filters): FiltersViewModel
    }
}
