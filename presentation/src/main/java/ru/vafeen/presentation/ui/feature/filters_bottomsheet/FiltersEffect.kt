package ru.vafeen.presentation.ui.feature.filters_bottomsheet

/**
 * Represents one-time side effects emitted by the Filters feature.
 */
sealed class FiltersEffect {

    /**
     * Effect emitted when filters have been applied.
     *
     * @param filters The [Filters] representing the applied filters.
     */
    data class FiltersApplied(val filters: Filters) : FiltersEffect()
}
