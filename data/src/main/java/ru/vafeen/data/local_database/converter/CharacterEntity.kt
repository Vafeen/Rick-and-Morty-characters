package ru.vafeen.data.local_database.converter

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.vafeen.data.local_database.entity.CharacterEntity
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Location
import java.time.DateTimeException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val json = Json { ignoreUnknownKeys = true }

/**
 * Converts [CharacterData] domain model to [CharacterEntity] database entity
 * @return [CharacterEntity] ready for database storage
 */
fun CharacterData.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    lifeStatus = lifeStatus,
    species = species,
    subtype = type,
    gender = gender,
    originName = origin.name,
    originId = origin.locationId,
    currentLocationName = currentLocation.name,
    currentLocationId = currentLocation.locationId,
    imageUrl = imageUrl,
    episodeIds = try {
        json.encodeToString(episodeIds)
    } catch (_: SerializationException) {
        "[]" // Fallback for serialization errors
    },
    apiUrl = apiUrl,
    createdAt = createdAt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
    nextKey = nextKey
)

/**
 * Converts [CharacterEntity] database entity back to [CharacterData] domain model
 * @return Fully reconstructed [CharacterData] domain object
 */
fun CharacterEntity.toCharacterData(): CharacterData = CharacterData(
    id = id,
    name = name,
    lifeStatus = lifeStatus,
    species = species,
    type = subtype,
    gender = gender,
    origin = Location(originName, originId),
    currentLocation = Location(currentLocationName, currentLocationId),
    imageUrl = imageUrl,
    episodeIds = try {
        json.decodeFromString(episodeIds)
    } catch (_: SerializationException) {
        emptyList() // Fallback for deserialization errors
    },
    apiUrl = apiUrl,
    createdAt = try {
        ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    } catch (_: DateTimeException) {
        ZonedDateTime.now() // Fallback for date parsing errors
    },
    nextKey = nextKey
)