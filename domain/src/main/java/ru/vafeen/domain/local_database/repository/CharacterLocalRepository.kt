package ru.vafeen.domain.local_database.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.model.CharacterData

/**
 * Defines the contract for local character data storage operations.
 * This repository handles all interactions with the local database for CharacterData entities.
 */
interface CharacterLocalRepository {
    /**
     * Retrieves all characters stored in the local database.
     * @return A Flow emitting List<CharacterData> that updates automatically
     *         when the underlying data changes.
     */
    fun getAll(): Flow<List<CharacterData>>

    suspend fun getPagedCharacters(offset: Int, limit: Int): List<CharacterData>

    fun getPaged(): Flow<PagingData<CharacterData>>

    /**
     * Inserts or updates characters in the local database.
     * @param characters List of character data to be inserted or updated
     */
    suspend fun insert(characters: List<CharacterData>)

    /**
     * Deletes characters from the local database.
     * @param characters List of character data to be deleted
     */
    suspend fun delete(characters: List<CharacterData>)

    suspend fun clear()

}