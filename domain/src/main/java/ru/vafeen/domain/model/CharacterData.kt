package ru.vafeen.domain.model

import java.time.ZonedDateTime

/**
 * Domain model representing a character in the Rick and Morty universe
 * @property id Unique character identifier
 * @property name Character's full name
 * @property lifeStatus Current life status
 * @property species Biological species classification
 * @property subtype Subspecies or variant (nullable)
 * @property gender Gender identity
 * @property origin Character's place of origin
 * @property currentLocation Character's last known location
 * @property imageUrl URL to character's avatar image
 * @property episodeIds List of episode IDs where character appears
 * @property apiUrl Direct URL to character's API endpoint
 * @property createdAt Timestamp when record was created
 */
data class CharacterData(
    val id: Int,
    val name: String,
    val lifeStatus: LifeStatus,
    val species: String,
    val subtype: String?,
    val gender: Gender,
    val origin: Location,
    val currentLocation: Location,
    val imageUrl: String,
    val episodeIds: List<Int>,
    val apiUrl: String,
    val createdAt: ZonedDateTime
) {
    /**
     * Location information container
     * @property name Common location name
     * @property locationId Unique location identifier (nullable)
     */
    data class Location(
        val name: String,
        val locationId: Int?
    )

    /**
     * Character life status enumeration
     */
    enum class LifeStatus {
        ALIVE, DEAD, UNKNOWN
    }

    /**
     * Gender identity enumeration
     */
    enum class Gender {
        MALE, FEMALE, GENDERLESS, UNKNOWN
    }
}