package ru.vafeen.domain.model

import java.time.ZonedDateTime
/**
 * Domain model representing a character in the Rick and Morty universe
 * @property id The id of the character
 * @property name The name of the character
 * @property lifeStatus The status of the character ('Alive', 'Dead' or 'unknown')
 * @property species The species of the character
 * @property type The type or subspecies of the character
 * @property gender The gender of the character ('Female', 'Male', 'Genderless' or 'unknown')
 * @property origin Name and link to the character's origin location
 * @property currentLocation Name and link to the character's last known location endpoint
 * @property imageUrl Link to the character's image (300x300px)
 * @property episodeIds List of episodes in which this character appeared
 * @property apiUrl Link to the character's own URL endpoint
 * @property createdAt Time at which the character was created in the database
 * @property nextKey Key for pagination support
 */
data class CharacterData(
    val id: Int,
    val name: String,
    val lifeStatus: LifeStatus,
    val species: String,
    val type: String?,
    val gender: Gender,
    val origin: Location,
    val currentLocation: Location,
    val imageUrl: String,
    val episodeIds: List<Int>,
    val apiUrl: String,
    val createdAt: ZonedDateTime,
    val nextKey: Int?,
)