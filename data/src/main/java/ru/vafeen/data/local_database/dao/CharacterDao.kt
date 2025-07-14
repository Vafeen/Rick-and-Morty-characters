package ru.vafeen.data.local_database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.CharacterEntity

/**
 * Data Access Object (DAO) for character entities in the local database.
 * Provides methods for CRUD operations and data retrieval with pagination support.
 *
 * Extends:
 * - [DataAccessObject] for basic CRUD operations
 * - [FlowGetAllImplementation] for Flow-based data streaming
 */
@Dao
internal interface CharacterDao : DataAccessObject<CharacterEntity>,
    FlowGetAllImplementation<CharacterEntity> {

    /**
     * Retrieves a paginated list of characters from the local database.
     *
     * @param offset The starting position of the pagination (0-based index)
     * @param limit The maximum number of items to return
     * @return List of [CharacterEntity] objects for the requested page
     *
     */
    @Query("SELECT * FROM characters LIMIT :limit OFFSET :offset")
    suspend fun getPagedCharacters(offset: Int, limit: Int): List<CharacterEntity>

    @Query("SELECT * FROM characters")
    fun getPagingSource(): PagingSource<Int, CharacterEntity>

    /**
     * Retrieves all characters from the local database as a continuous stream.
     * The Flow emits a new list whenever the underlying data changes.
     *
     * @return [Flow] emitting the complete list of [CharacterEntity] objects
     *
     * @see FlowGetAllImplementation.getAll
     */
    @Query("SELECT * FROM characters")
    override fun getAll(): Flow<List<CharacterEntity>>

    /**
     * Clears all character entries from the local database.
     * This operation is irreversible and should be used with caution.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM characters")
    override suspend fun clear()
}