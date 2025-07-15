package ru.vafeen.data.local_database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.CharacterEntity

/**
 * Data Access Object (DAO) for [CharacterEntity] operations in the local Room database.
 * Provides reactive and paginated access to character data with automatic change notifications.
 *
 * Combines:
 * - Standard CRUD via [DataAccessObject]
 * - Reactive streams via [FlowGetAllImplementation]
 * - Custom pagination methods
 */
@Dao
internal interface CharacterDao : DataAccessObject<CharacterEntity>,
    FlowGetAllImplementation<CharacterEntity> {

    /**
     * Retrieves a paginated subset of characters using offset/limit strategy.
     *
     * @param offset The starting position in the dataset (0-based index).
     * @param limit Maximum number of items to return.
     * @return List of [CharacterEntity] for the requested page.
     *         Returns an empty list if no data exists at the specified offset.
     *
     * @sample ru.vafeen.data.repository.CharacterRepositoryImpl.getPagedCharacters
     */
    @Query("SELECT * FROM characters LIMIT :limit OFFSET :offset")
    suspend fun getPagedCharacters(offset: Int, limit: Int): List<CharacterEntity>

    /**
     * Creates a [PagingSource] for integration with Jetpack Paging 3 library.
     *
     * @return Configured [PagingSource] that loads [CharacterEntity] items
     *         with integer keys representing page indices.
     */
    @Query(
        """
    SELECT * FROM characters 
    WHERE 
        (:name IS NULL OR name LIKE '%' || :name || '%') AND
        (:status IS NULL OR life_status = :status) AND
        (:species IS NULL OR species = :species) AND
        (:type IS NULL OR subtype = :type) AND
        (:gender IS NULL OR gender = :gender)
"""
    )
    fun getPagingSource(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): PagingSource<Int, CharacterEntity>

    /**
     * Provides reactive stream of all characters with automatic updates.
     *
     * @return [Flow] emitting the complete dataset that automatically
     *         updates on any changes to the 'characters' table.
     *         Emits empty list if table is empty.
     *
     * @see FlowGetAllImplementation.getAll
     */
    @Query("SELECT * FROM characters")
    override fun getAll(): Flow<List<CharacterEntity>>

    /**
     * Deletes all records from the characters table.
     * This operation resets all auto-increment counters.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM characters")
    override suspend fun clear()

    /**
     * Counts total character records in the database.
     *
     * @return Total number of [CharacterEntity] records.
     *         Returns 0 if the table is empty.
     */
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharactersCount(): Int

    /**
     * Retrieves a single character by its unique [id].
     *
     * @param id The unique identifier of the character.
     * @return The [CharacterEntity] corresponding to the given [id], or null if not found.
     */
    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacter(id: Int): CharacterEntity?
}
