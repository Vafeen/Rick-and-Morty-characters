package ru.vafeen.data.network.repository

import ru.vafeen.data.network.converter.toDomain
import ru.vafeen.data.network.getResponseResultWrappedAllErrors
import ru.vafeen.data.network.service.AllCharactersService
import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.AllCharactersRepository
import javax.inject.Inject

/**
 * Network implementation of AllCharactersRepository using Retrofit
 * @property service Retrofit service for API communication
 */
internal class RetrofitAllCharactersRepository @Inject constructor(
    private val service: AllCharactersService
) : AllCharactersRepository {

    /**
     * Fetches characters with error handling and DTO conversion
     * @param page Page number to fetch
     * @return ResponseResult containing domain models or error
     */
    override suspend fun getAllCharacters(page: Int):
            ResponseResult<Pair<PaginationInfo, List<CharacterData>>> =
        getResponseResultWrappedAllErrors {
            service.getCharacters(page).toDomain()
        }
}