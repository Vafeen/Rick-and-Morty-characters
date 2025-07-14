package ru.vafeen.data.network.converter

import ru.vafeen.data.network.dto.CharacterDataDTO
import ru.vafeen.data.network.dto.CharacterResponse
import ru.vafeen.data.network.dto.GenderDTO
import ru.vafeen.data.network.dto.LifeStatusDTO
import ru.vafeen.data.network.dto.SingleCharacterDTO
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.domain.model.Location
import ru.vafeen.domain.model.PaginationInfo
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

/**
 * Converts API response to domain model with pagination information
 * @return Pair of pagination information and character list
 * @throws IllegalStateException if pagination info is invalid
 */
fun CharacterResponse.toDomain(): Pair<PaginationInfo, List<CharacterData>> {
    val pagination = PaginationInfo(
        totalCount = info.totalCount,
        totalPages = info.totalPages,
        nextPage = info.nextPageUrl?.extractPageNumber(),
        prevPage = info.prevPageUrl?.extractPageNumber()
    )

    return pagination to (results?.map {
        it.toDomain(pagination.nextPage)
    } ?: emptyList())
}

/**
 * Converts character DTO to domain model
 * @return Domain model representation of the character
 * @throws DateTimeParseException if created date is invalid
 */
fun CharacterDataDTO.toDomain(nextKey: Int?): CharacterData = CharacterData(
    id = id,
    name = name,
    lifeStatus = status.toDomain(),
    species = species,
    type = type.ifEmpty { null },
    gender = gender.toDomain(),
    origin = Location(origin.name, origin.url.extractId()),
    currentLocation = Location(location.name, location.url.extractId()),
    imageUrl = image,
    episodeIds = episode.mapNotNull { it.extractId() },
    apiUrl = url,
    createdAt = ZonedDateTime.parse(created),
    nextKey = nextKey
)

/**
 * Converts single character DTO to domain model
 * @return Domain model representation of the character
 */
fun SingleCharacterDTO.toDomain(): CharacterData = CharacterData(
    id = id,
    name = name,
    lifeStatus = status.toDomain(),
    species = species,
    type = type.ifEmpty { null },
    gender = gender.toDomain(),
    origin = Location(origin.name, origin.url.extractId()),
    currentLocation = Location(location.name, location.url.extractId()),
    imageUrl = image,
    episodeIds = episode.mapNotNull { it.extractId() },
    apiUrl = url,
    createdAt = ZonedDateTime.parse(created),
    nextKey = null
)

/* Extension functions for enum conversions */
private fun LifeStatusDTO.toDomain() = when (this) {
    LifeStatusDTO.ALIVE -> LifeStatus.ALIVE
    LifeStatusDTO.DEAD -> LifeStatus.DEAD
    LifeStatusDTO.UNKNOWN -> LifeStatus.UNKNOWN
}

private fun GenderDTO.toDomain() = when (this) {
    GenderDTO.MALE -> Gender.MALE
    GenderDTO.FEMALE -> Gender.FEMALE
    GenderDTO.GENDERLESS -> Gender.GENDERLESS
    GenderDTO.UNKNOWN -> Gender.UNKNOWN
}

/* URL processing extensions */
private fun String.extractId(): Int? = split("/").lastOrNull()?.toIntOrNull()

private fun String.extractPageNumber(): Int? =
    split("page=").lastOrNull()?.takeWhile { it.isDigit() }?.toIntOrNull()