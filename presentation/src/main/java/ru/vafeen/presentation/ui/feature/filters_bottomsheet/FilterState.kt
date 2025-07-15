package ru.vafeen.presentation.ui.feature.filters_bottomsheet

import ru.vafeen.domain.model.Settings

data class FilterState(
    val filters: Filters,
    val settings: Settings,
)
