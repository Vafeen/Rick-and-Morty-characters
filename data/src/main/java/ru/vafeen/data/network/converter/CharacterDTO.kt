package ru.vafeen.data.network.converter

import ru.vafeen.data.network.dto.CharacterDataDTO
import ru.vafeen.data.network.dto.CharacterResponse
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import java.time.ZonedDateTime

/**
 * Converts API response to domain model with pagination information
 * @receiver CharacterResponse API response object
 * @return Pair containing pagination info and list of domain character models
 */
fun CharacterResponse.toDomain(): Pair<PaginationInfo, List<CharacterData>> = PaginationInfo(
    totalCount = info.totalCount,
    totalPages = info.totalPages,
    nextPage = info.nextPageUrl?.extractPageNumber(),
    prevPage = info.prevPageUrl?.extractPageNumber()
) to results.map { it.toDomain() }

/**
 * Converts single Character DTO to domain model
 * @receiver CharacterDataDTO API character object
 * @return Domain model representation of the character
 */
fun CharacterDataDTO.toDomain(): CharacterData = CharacterData(
    id = id,
    name = name,
    lifeStatus = when (status) {
        CharacterDataDTO.Status.ALIVE -> CharacterData.LifeStatus.ALIVE
        CharacterDataDTO.Status.DEAD -> CharacterData.LifeStatus.DEAD
        CharacterDataDTO.Status.UNKNOWN -> CharacterData.LifeStatus.UNKNOWN
    },
    species = species,
    subtype = type.ifEmpty { null },
    gender = when (gender) {
        CharacterDataDTO.Gender.MALE -> CharacterData.Gender.MALE
        CharacterDataDTO.Gender.FEMALE -> CharacterData.Gender.FEMALE
        CharacterDataDTO.Gender.GENDERLESS -> CharacterData.Gender.GENDERLESS
        CharacterDataDTO.Gender.UNKNOWN -> CharacterData.Gender.UNKNOWN
    },
    origin = CharacterData.Location(origin.name, origin.url.extractId()),
    currentLocation = CharacterData.Location(location.name, location.url.extractId()),
    imageUrl = image,
    episodeIds = episode.mapNotNull { it.extractId() },
    apiUrl = url,
    createdAt = ZonedDateTime.parse(created)
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