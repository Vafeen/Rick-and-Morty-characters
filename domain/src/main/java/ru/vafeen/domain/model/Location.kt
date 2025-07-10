package ru.vafeen.domain.model

/**
 * Location information container
 * @property name Common location name
 * @property locationId Unique location identifier (nullable)
 */
data class Location(
    val name: String,
    val locationId: Int?
)