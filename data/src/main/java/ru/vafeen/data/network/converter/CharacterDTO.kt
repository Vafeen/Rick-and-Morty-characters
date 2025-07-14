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

/**
 * Converts API response to domain model with pagination information
 * @receiver CharacterResponse API response object
 * @return Pair containing pagination info and list of domain character models
 */
fun CharacterResponse.toDomain(): Pair<PaginationInfo, List<CharacterData>> {
    val pagination = PaginationInfo(
        totalCount = info.totalCount,
        totalPages = info.totalPages,
        nextPage = info.nextPageUrl?.extractPageNumber(),
        prevPage = info.prevPageUrl?.extractPageNumber()
    )

    // Обработка случая, когда results = null (хотя API Rick and Morty всегда возвращает массив)
    val characters = results?.map { it.toDomain(pagination.nextPage) } ?: emptyList()

    return pagination to characters
}

/**
 * Converts single Character DTO to domain model
 * @receiver CharacterDataDTO API character object
 * @return Domain model representation of the character
 */
fun CharacterDataDTO.toDomain(nextKey: Int?): CharacterData = CharacterData(
    id = id,
    name = name,
    lifeStatus = when (status) {
        LifeStatusDTO.ALIVE -> LifeStatus.ALIVE
        LifeStatusDTO.DEAD -> LifeStatus.DEAD
        LifeStatusDTO.UNKNOWN -> LifeStatus.UNKNOWN
    },
    species = species,
    subtype = type.ifEmpty { null },
    gender = when (gender) {
        GenderDTO.MALE -> Gender.MALE
        GenderDTO.FEMALE -> Gender.FEMALE
        GenderDTO.GENDERLESS -> Gender.GENDERLESS
        GenderDTO.UNKNOWN -> Gender.UNKNOWN
    },
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
 * @receiver SingleCharacterDTO API character object
 * @return Domain model representation of the character
 */
fun SingleCharacterDTO.toDomain(): CharacterData = CharacterData(
    id = id,
    name = name,
    lifeStatus = when (status) {
        LifeStatusDTO.ALIVE -> LifeStatus.ALIVE
        LifeStatusDTO.DEAD -> LifeStatus.DEAD
        LifeStatusDTO.UNKNOWN -> LifeStatus.UNKNOWN
    },
    species = species,
    subtype = type.ifEmpty { null },
    gender = when (gender) {
        GenderDTO.MALE -> Gender.MALE
        GenderDTO.FEMALE -> Gender.FEMALE
        GenderDTO.GENDERLESS -> Gender.GENDERLESS
        GenderDTO.UNKNOWN -> Gender.UNKNOWN
    },
    origin = Location(
        name = origin.name,
        locationId = origin.url.extractId()
    ),
    currentLocation = Location(
        name = location.name,
        locationId = location.url.extractId()
    ),
    imageUrl = image,
    episodeIds = episode.mapNotNull { it.extractId() },
    apiUrl = url,
    createdAt = ZonedDateTime.parse(created),
    nextKey = null // todo remove it
)

/**
 * Extracts resource ID from API URL
 * @receiver API URL string
 * @return Extracted ID or null if URL is malformed
 */
private fun String.extractId(): Int? = split("/").lastOrNull()?.toIntOrNull()

/**
 * Extracts page number from pagination URL
 * @receiver Pagination URL string
 * @return Extracted page number or null if URL is malformed
 */
private fun String.extractPageNumber(): Int? =
    split("page=").lastOrNull()?.toIntOrNull()