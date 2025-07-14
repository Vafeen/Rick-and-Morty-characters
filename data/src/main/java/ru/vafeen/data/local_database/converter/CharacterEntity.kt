package ru.vafeen.data.local_database.converter

import kotlinx.serialization.json.Json
import ru.vafeen.data.local_database.entity.CharacterEntity
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Location
import java.time.ZonedDateTime


fun CharacterData.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    lifeStatus = lifeStatus,
    species = species,
    subtype = subtype,
    gender = gender,
    originName = origin.name,
    originId = origin.locationId,
    currentLocationName = currentLocation.name,
    currentLocationId = currentLocation.locationId,
    imageUrl = imageUrl,
    episodeIds = Json.encodeToString(episodeIds),
    apiUrl = apiUrl,
    createdAt = createdAt.toString(),
    nextKey = nextKey
)

// Преобразование CharacterEntity в CharacterData
fun CharacterEntity.toCharacterData(): CharacterData = CharacterData(
    id = id,
    name = name,
    lifeStatus = lifeStatus,
    species = species,
    subtype = subtype,
    gender = gender,
    origin = Location(originName, originId),
    currentLocation = Location(currentLocationName, currentLocationId),
    imageUrl = imageUrl,
    episodeIds = Json.decodeFromString(episodeIds),
    apiUrl = apiUrl,
    createdAt = ZonedDateTime.parse(createdAt),
    nextKey = nextKey,
)