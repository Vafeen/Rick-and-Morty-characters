package ru.vafeen.data.network.repository

import retrofit2.HttpException
import ru.vafeen.data.network.converter.toDomain
import ru.vafeen.data.network.getResponseResultWrappedAllErrors
import ru.vafeen.data.network.service.CharacterService
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.CharacterRemoteRepository
import javax.inject.Inject

/**
 * Network implementation of [CharacterRemoteRepository] using Retrofit service.
 * Handles all character-related network operations including:
 * - Filtered character searches with pagination
 * - Single character lookups
 * - Error handling and response conversion
 *
 * @property service The Retrofit [CharacterService] instance for API communication
 */
internal class RetrofitCharacterRemoteRepository @Inject constructor(
    private val service: CharacterService
) : CharacterRemoteRepository {

    /**
     * Executes filtered character search with comprehensive error handling.
     *
     * Features:
     * - Converts API DTOs to domain models
     * - Handles special 404 case (empty result)
     * - Wraps other HTTP errors
     * - Preserves pagination metadata
     *
     * @param name Case-insensitive name filter (null to skip)
     * @param status Life status filter ("alive"/"dead"/"unknown")
     * @param species Species type filter (e.g. "Human")
     * @param type Subspecies filter (e.g. "Clone")
     * @param gender Gender filter ("female"/"male"/etc)
     * @param page 1-based page number
     *
     * @return [ResponseResult] containing:
     * - Success: Pair of [PaginationInfo] and filtered [CharacterData] list
     * - Error: Formatted error message for network/HTTP failures
     *
     * @see CharacterService.filterCharacters
     */
    override suspend fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?,
        page: Int
    ): ResponseResult<Pair<PaginationInfo, List<CharacterData>>> = try {
        ResponseResult.Success(
            data = service.filterCharacters(
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender,
                page = page
            ).toDomain()
        )
    } catch (e: HttpException) {
        if (e.code() == 404) {
            ResponseResult.Success(
                data = PaginationInfo(
                    totalCount = 0,
                    totalPages = 0,
                    nextPage = null,
                    prevPage = null
                ) to emptyList()
            )
        } else ResponseResult.Error(stacktrace = "HTTP error ${e.code()}: ${e.message()}")
    } catch (e: Exception) {
        ResponseResult.Error(stacktrace = "Network error: ${e.localizedMessage}")
    }

    /**
     * Retrieves single character details with standardized error handling.
     *
     * @param id Character ID (must be positive integer)
     * @return [ResponseResult] containing:
     * - Success: [CharacterData] for requested ID
     * - Error: Formatted error message for:
     *   - HTTP 404 (Not Found)
     *   - Network failures
     *   - Invalid responses
     *
     * @see getResponseResultWrappedAllErrors
     * @see CharacterService.getCharacter
     */
    override suspend fun getCharacter(id: Int): ResponseResult<CharacterData> =
        getResponseResultWrappedAllErrors {
            service.getCharacter(id).toDomain()
        }
}