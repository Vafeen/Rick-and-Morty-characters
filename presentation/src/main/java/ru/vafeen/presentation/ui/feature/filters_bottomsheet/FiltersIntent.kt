package ru.vafeen.presentation.ui.feature.filters_bottomsheet

import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus

/**
 * Represents user intents (actions) to modify filter parameters in the filters bottom sheet.
 */
sealed class FiltersIntent {
    /**
     * Intent to change the character name filter.
     *
     * @param name The new name filter value.
     */
    data class NameChanged(val name: String) : FiltersIntent()

    /**
     * Intent to change the character life status filter.
     *
     * @param status The new life status filter value, or null to clear filter.
     */
    data class StatusChanged(val status: LifeStatus?) : FiltersIntent()

    /**
     * Intent to change the character species filter.
     *
     * @param species The new species filter value.
     */
    data class SpeciesChanged(val species: String) : FiltersIntent()

    /**
     * Intent to change the character type filter.
     *
     * @param type The new type filter value.
     */
    data class TypeChanged(val type: String) : FiltersIntent()

    /**
     * Intent to change the character gender filter.
     *
     * @param gender The new gender filter value, or null to clear filter.
     */
    data class GenderChanged(val gender: Gender?) : FiltersIntent()

    /**
     * Intent to apply the currently selected filters.
     */
    object ApplyFilters : FiltersIntent()

    /**
     * Intent to reset all filters to default values.
     */
    object ResetFilters : FiltersIntent()
}
