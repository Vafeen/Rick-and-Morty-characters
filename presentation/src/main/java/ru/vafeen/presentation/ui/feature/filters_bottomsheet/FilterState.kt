package ru.vafeen.presentation.ui.feature.filters_bottomsheet

import ru.vafeen.domain.model.Settings

/**
 * Represents the combined state of current filters and app settings in the filters bottom sheet.
 *
 * @property filters The currently applied filter criteria.
 * @property settings The application settings relevant to filter behavior and appearance.
 */
data class FilterState(
    val filters: Filters,
    val settings: Settings,
)
