package ru.vafeen.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.vafeen.data.network.dto.CharacterResponse

/**
 * Retrofit service interface for character API endpoints
 */
interface AllCharactersService {
    /**
     * Fetches paginated character list
     * @param page Page number to retrieve (default: 1)
     * @return CharacterResponse with pagination info and results
     */
    @GET(GET_ALL_CHARACTERS)
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): CharacterResponse

    companion object {
        private const val GET_ALL_CHARACTERS = "character"
    }
}