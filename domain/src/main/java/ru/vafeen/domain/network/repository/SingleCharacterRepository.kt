package ru.vafeen.domain.network.repository

import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.network.ResponseResult

/**
 * Repository for retrieving single character data from network source.
 * Provides abstraction over network operations for character data fetching.
 */
interface SingleCharacterRepository {
    /**
     * Fetches character data by ID from remote source.
     *
     * @param id Unique identifier of the character to fetch
     * @return ResponseResult containing either CharacterData on success
     *         or Exception on failure
     */
    suspend fun getCharacter(id: Int): ResponseResult<CharacterData>
}