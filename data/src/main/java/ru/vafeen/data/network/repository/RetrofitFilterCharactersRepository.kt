package ru.vafeen.data.network.repository

import retrofit2.HttpException
import ru.vafeen.data.network.converter.toDomain
import ru.vafeen.data.network.service.FilterCharactersService
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.FilterCharactersRepository
import javax.inject.Inject

/**
 * Network implementation of [FilterCharactersRepository] using Retrofit.
 * Handles API communication and response conversion for character filtering operations.
 *
 * @property service Retrofit service instance for character API endpoints
 */
internal class RetrofitFilterCharactersRepository @Inject constructor(
    private val service: FilterCharactersService
) : FilterCharactersRepository {

    /**
     * Executes filtered character request with error handling and data conversion.
     *
     * Implementation details:
     * 1. Validates parameters through API
     * 2. Handles network errors gracefully
     * 3. Converts DTOs to domain models
     * 4. Preserves pagination information
     *
     * @param name Name filter parameter
     * @param status Status filter parameter
     * @param species Species filter parameter
     * @param type Type filter parameter
     * @param gender Gender filter parameter
     * @param page Page number
     * @return [ResponseResult] with parsed data or error
     */
    override suspend fun filterCharacters(
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
}