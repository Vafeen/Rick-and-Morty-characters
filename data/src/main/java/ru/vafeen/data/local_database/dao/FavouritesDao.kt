package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.CharacterEntity
import ru.vafeen.data.local_database.entity.FavouritesEntity


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
internal interface FavouritesDao : DataAccessObject<FavouritesEntity>,
    FlowGetAllImplementation<FavouritesEntity> {

    /**
     * Provides reactive stream of all characters with automatic updates.
     *
     * @return [Flow] emitting the complete dataset that automatically
     *         updates on any changes to the 'characters' table.
     *         Emits empty list if table is empty.
     *
     * @see FlowGetAllImplementation.getAll
     */
    @Query("SELECT * FROM favourites")
    override fun getAll(): Flow<List<FavouritesEntity>>

    /**
     * Deletes all records from the characters table.
     * This operation resets all auto-increment counters.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM favourites")
    override suspend fun clear()

}
