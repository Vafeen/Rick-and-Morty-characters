package ru.vafeen.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.vafeen.data.network.dto.CharacterResponse

/**
 * Retrofit service interface for character filtering endpoints.
 * Maps HTTP requests to API calls for character filtering operations.
 */
interface FilterCharactersService {
    /**
     * Fetches paginated, filtered character list from API.
     *
     * @param name Filter by character name (partial match)
     * @param status Filter by life status
     * @param species Filter by species type
     * @param type Filter by subspecies/variant
     * @param gender Filter by gender
     * @param page Page number to retrieve (default: 1)
     * @return [CharacterResponse] with pagination info and results
     *
     * API Endpoint: GET /character/
     * Example: /character/?name=rick&status=alive&page=2
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
}