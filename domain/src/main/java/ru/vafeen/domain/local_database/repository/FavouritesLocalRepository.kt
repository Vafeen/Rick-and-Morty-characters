package ru.vafeen.domain.local_database.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing favourite characters locally.
 */
interface FavouritesLocalRepository {

    /**
     * Adds a character to the favourites list.
     *
     * @param id The unique identifier of the character to add.
     */
    suspend fun addToFavourites(id: Int)

    /**
     * Removes a character from the favourites list.
     *
     * @param id The unique identifier of the character to remove.
     */
    suspend fun removeFromFavourites(id: Int)

    /**
     * Retrieves all favourite character IDs as a reactive stream.
     *
     * @return A [Flow] emitting list of character IDs currently marked as favourites.
     */
    fun getAllFavourites(): Flow<List<Int>>
}
