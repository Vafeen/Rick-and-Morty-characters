package ru.vafeen.data.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import ru.vafeen.data.network.dto.SingleCharacterDTO

/**
 * Retrofit service interface for fetching single character data
 */
interface SingleCharacterService {
    /**
     * Fetches a single character by ID
     * @param id The ID of the character to fetch
     * @return SingleCharacterDTO containing character data
     */
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): SingleCharacterDTO
}