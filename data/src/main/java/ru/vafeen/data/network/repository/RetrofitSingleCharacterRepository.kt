package ru.vafeen.data.network.repository

import ru.vafeen.data.network.converter.toDomain
import ru.vafeen.data.network.getResponseResultWrappedAllErrors
import ru.vafeen.data.network.service.SingleCharacterService
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.SingleCharacterRepository
import javax.inject.Inject

/**
 * Network implementation of SingleCharacterRepository
 * @property service Retrofit service for character API
 */
internal class RetrofitSingleCharacterRepository @Inject constructor(
    private val service: SingleCharacterService
) : SingleCharacterRepository {

    /**
     * Fetches a single character with error handling
     * @param id Character ID to fetch
     * @return ResponseResult containing either:
     *   - Success with CharacterData
     *   - Error with stacktrace
     */
    override suspend fun getCharacter(id: Int): ResponseResult<CharacterData> =
        getResponseResultWrappedAllErrors {
            service.getCharacter(id).toDomain()
        }
}