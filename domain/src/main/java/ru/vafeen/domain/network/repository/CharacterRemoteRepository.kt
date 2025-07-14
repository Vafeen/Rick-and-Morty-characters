package ru.vafeen.domain.network.repository

import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ResponseResult

/**
 * Network repository interface for character data operations.
 * Provides access to remote character API with filtering and pagination capabilities.
 *
 * Main features:
 * - Filter characters by multiple criteria
 * - Paginated results
 * - Single character lookup
 * - Error handling through ResponseResult
 */
interface CharacterRemoteRepository {
    /**
     * Retrieves paginated list of characters with optional filters.
     *
     * @param name Case-insensitive partial name match (e.g. "Rick" matches "Rick Sanchez")
     * @param status Life status filter ("alive", "dead", or "unknown")
     * @param species Species filter (e.g. "Human", "Alien")
     * @param type Subspecies/variant filter (e.g. "Parasite")
     * @param gender filter ("female", "male", "genderless", or "unknown")
     * @param page 1-based page index for pagination
     *
     * @return [ResponseResult] with:
     * - Success: Pair of [PaginationInfo] and filtered [CharacterData] list
     * - Error: Exception details on failure
     *
     * @sample ru.vafeen.data.network.repository.RetrofitCharacterRemoteRepository.getCharacters
     */
    suspend fun getCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null,
        page: Int = 1
    ): ResponseResult<Pair<PaginationInfo, List<CharacterData>>>

    /**
     * Fetches detailed information for a single character.
     *
     * @param id The unique identifier of the character
     * @return [ResponseResult] with:
     * - Success: Complete [CharacterData] for requested ID
     * - Error: Exception if character not found or network failure
     *
     * @throws IllegalArgumentException if ID ≤ 0
     *
     * @see ru.vafeen.data.network.repository.RetrofitCharacterRepository.getCharacter
     */
    suspend fun getCharacter(id: Int): ResponseResult<CharacterData>
}