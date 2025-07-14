package ru.vafeen.domain.network.repository

import ru.vafeen.domain.model.CharacterData
import ru.vafeen.domain.model.PaginationInfo
import ru.vafeen.domain.network.ResponseResult

/**
 * Network repository interface for filtering characters through API with various criteria.
 * Provides methods to retrieve paginated character data based on multiple filter parameters.
 */
interface FilterCharactersRepository {
    /**
     * Retrieves characters matching specified filters with pagination support.
     *
     * @param name Filter by character name (case-insensitive partial match).
     * Example: "Rick" will match "Rick Sanchez", "Rick D. Sanchez", etc.
     * @param status Filter by life status. Valid values: "alive", "dead", "unknown".
     * @param species Filter by species. Example: "Human", "Alien", "Humanoid".
     * @param type Filter by subspecies or variant. Example: "Parasite", "Genetic experiment".
     * @param gender Filter by gender. Valid values: "female", "male", "genderless", "unknown".
     * @param page Page number for paginated results (1-based index).
     * @return [ResponseResult] containing either:
     *   - Success with [Pair] of [PaginationInfo] and filtered [CharacterData] list
     *   - Error with failure details
     */
    suspend fun filterCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null,
        page: Int = 1
    ): ResponseResult<Pair<PaginationInfo, List<CharacterData>>>
}