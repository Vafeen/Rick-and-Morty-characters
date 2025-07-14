package ru.vafeen.domain.local_database.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.model.CharacterData

/**
 * Defines the contract for local character data storage operations.
 * This repository handles all interactions with the local database for [CharacterData] entities,
 * including CRUD operations and paginated data access.
 */
interface CharacterLocalRepository {

    /**
     * Retrieves all characters from the local database as a continuously updating stream.
     *
     * @return A [Flow] emitting a [List] of [CharacterData] that automatically updates
     *         when the underlying data changes. The Flow remains active until explicitly canceled.
     */
    fun getAll(): Flow<List<CharacterData>>

    /**
     * Retrieves a paginated subset of characters from the local database.
     *
     * @param offset The starting position of the query (0-based index).
     * @param limit The maximum number of items to return.
     * @return A [List] of [CharacterData] for the requested page, or an empty list if no data exists.
     */
    suspend fun getPagedCharacters(offset: Int, limit: Int): List<CharacterData>

    /**
     * Provides a [Flow] of paginated character data for use with Jetpack Paging.
     *
     * @return A [Flow] of [PagingData]<[CharacterData]> that can be consumed by Paging's UI components.
     */
    fun getPaged(): Flow<PagingData<CharacterData>>

    /**
     * Inserts or updates characters in the local database.
     * If a character with the same primary key already exists, it will be replaced.
     *
     * @param characters The list of [CharacterData] entities to upsert.
     */
    suspend fun insert(characters: List<CharacterData>)

    /**
     * Removes specific characters from the local database.
     *
     * @param characters The list of [CharacterData] entities to delete.
     * @throws androidx.room.RoomDatabase if the operation fails.
     */
    suspend fun delete(characters: List<CharacterData>)

    /**
     * Clears all character data from the local database.
     */
    suspend fun clear()

    /**
     * Counts the total number of characters stored in the local database.
     *
     * @return The total count of [CharacterData] entities, or 0 if the table is empty.
     */
    suspend fun getCharactersCount(): Int
}