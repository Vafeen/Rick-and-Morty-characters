package ru.vafeen.data.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vafeen.data.network.dto.CharacterResponse
import ru.vafeen.data.network.dto.SingleCharacterDTO

/**
 * Retrofit service interface for character-related API endpoints.
 * Defines network operations for the character including filtering and single character lookup.
 *
 * All methods are suspend functions and should be called from coroutines.
 */
interface CharacterService {

    /**
     * Retrieves paginated and filtered character list from the API.
     *
     * @param name Filter by character name (case-insensitive contains match)
     * @param status Filter by life status. Valid values: "alive", "dead", "unknown"
     * @param species Filter by species type (e.g. "Human", "Alien")
     * @param type Filter by subspecies or variant (e.g. "Parasite", "Clone")
     * @param gender Filter by gender. Valid values: "female", "male", "genderless", "unknown"
     * @param page 1-based page index for pagination (default: 1)
     *
     * @return [CharacterResponse] containing:
     * - [CharacterResponse.info] Pagination metadata
     * - [CharacterResponse.results] List of matching characters
     *
     * @throws retrofit2.HttpException for 4XX/5XX responses
     *
     * API Specification:
     * - Endpoint: GET /character/
     * - Parameters: All query parameters are optional
     * - Example Request: `/character/?name=rick&status=alive&page=2`
     */
    @GET("character/")
    suspend fun filterCharacters(
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null,
        @Query("page") page: Int = 1
    ): CharacterResponse

    /**
     * Retrieves detailed information for a single character by ID.
     *
     * @param id The unique identifier of the character (positive integer)
     * @return [SingleCharacterDTO] containing complete character data
     *
     * @throws retrofit2.HttpException for 404 (Not Found) or other error statuses
     * @throws IllegalArgumentException if ID ≤ 0
     *
     * API Specification:
     * - Endpoint: GET /character/{id}
     * - Example Request: `/character/123`
     */
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): SingleCharacterDTO
}