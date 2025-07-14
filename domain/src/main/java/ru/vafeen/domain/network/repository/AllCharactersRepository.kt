package ru.vafeen.domain.network.repository


import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ResponseResult

/**
 * Interface for character data network repository operations
 */
interface AllCharactersRepository {
    /**
     * Fetches paginated list of characters
     * @param page Page number to fetch (default: 1)
     * @return ResponseResult containing either:
     *   - Success with Pair of PaginationInfo and CharacterData list
     *   - Failure with error information
     */
    suspend fun getAllCharacters(page: Int = 1): ResponseResult<Pair<PaginationInfo, List<CharacterData>>>
}