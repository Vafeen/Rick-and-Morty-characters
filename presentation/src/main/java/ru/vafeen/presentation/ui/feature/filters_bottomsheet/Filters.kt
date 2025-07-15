package ru.vafeen.presentation.ui.feature.filters_bottomsheet

import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus

/**
 * Represents the current state of filters applied in the filters bottom sheet.
 *
 * @property name Filter by character name (nullable).
 * @property status Filter by character life status (nullable).
 * @property species Filter by character species (nullable).
 * @property type Filter by character type (nullable).
 * @property gender Filter by character gender (nullable).
 */
data class Filters(
    val name: String? = null,
    val status: LifeStatus? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: Gender? = null,
)
